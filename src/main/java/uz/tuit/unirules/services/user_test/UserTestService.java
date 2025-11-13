package uz.tuit.unirules.services.user_test;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.respond_dto.QuestionOptionUserRespDto;
import uz.tuit.unirules.dto.respond_dto.UserQuestionDto;
import uz.tuit.unirules.dto.respond_dto.UserQuestionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.entity.user_test.UserTest;
import uz.tuit.unirules.entity.user_test.UserTestAnswer;
import uz.tuit.unirules.entity.user_test.UserTestRepository;
import uz.tuit.unirules.handler.exceptions.CustomException;
import uz.tuit.unirules.repository.UserTestAnswerRepository;
import uz.tuit.unirules.repository.test.QuestionOptionRepository;
import uz.tuit.unirules.repository.test.QuestionRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.test.QuestionService;
import uz.tuit.unirules.services.test.TestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserTestService {
    private final AuthUserService authUserService;
    private final UserTestRepository userTestRepository;
    private final TestService testService;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final UserTestAnswerRepository userTestAnswerRepository;
    private final QuestionOptionRepository questionOptionRepository;

    public void startTest(Long moduleId) {
        User authUser = authUserService.getAuthUser();
        Test test = testService.findByModuleId(moduleId);
        Optional<UserTest> userTest = userTestRepository.findByUserIdAndTestId(authUser.getId(), test.getId());
        if (userTest.isEmpty()) {
            createUserTest(authUser, test);
        } else {
            UserTest userTest1 = userTest.get();
            if (!userTest1.isTestTimeOver()) {
                throw new CustomException("test has not finished yet !!!", HttpStatus.FORBIDDEN, "TEST_HAS_NOT_FINISHED");
            }
            userTest1.setStartedAt(LocalDateTime.now());
            userTestRepository.save(userTest1);
        }
    }


    private UserTest createUserTest(User authUser, Test test) {
        return userTestRepository.save(UserTest.builder()
                .isDeleted(false)
                .success(null)
                .test(test)
                .startedAt(LocalDateTime.now())
                .user(authUser)
                .build());
    }

    @Transactional
    public void finishTest(Long testId) {
        // vaqt tugagan bolsa ham bu ishlaydi, chunki vaqt tugagandan keyin javobni ozgartrolmaydi
        User authUser = authUserService.getAuthUser();
        UserTest userTest = userTestRepository.findByUserIdAndTestId(authUser.getId(), testId)
                .orElseThrow(() -> new RuntimeException("User Test topilmadi"));
        /*if (userTest.isTestTimeOver()) {
            throw new CustomException("Testning amal qilish muddati tugagan", HttpStatus.GONE, "TEST_DEADLINE_EXPIRED");
        }*/
       /* if (userTest.getFinishedAt() != null) {
            throw new CustomException("Test allaqachon yakunlangan", HttpStatus.FORBIDDEN, "TEST_ALREADY_FINISHED");
        }*/
        userTest.setFinishedAt(LocalDateTime.now());
        calculateResult(userTest);
        userTestRepository.save(userTest);
    }

    private void calculateResult(UserTest userTest) {
        Long testId = userTest.getTest().getId();

        int totalQuestions = questionRepository.countByTestIdAndIsDeletedFalse(testId);
        if (totalQuestions == 0) {
            userTest.setResult(0f);
            userTest.setSuccess(false);
            userTestRepository.save(userTest);
            return;
        }

        long correctAnswers = userTestAnswerRepository.countByUserTestIdAndQuestionOption_IsCorrectTrueAndIsDeletedFalse(userTest.getId());

        float percentage = ((float) correctAnswers / totalQuestions) * 100;

        userTest.setResult(percentage);
        userTest.setSuccess(percentage >= 60);
        userTestRepository.save(userTest);
    }


    @Transactional(readOnly = true)
    public List<UserQuestionDto> getUserQuestions(Long moduleId) {
        Long userId = authUserService.getAuthUserId();

        // 1. Testni va UserTest ni topamiz
        Test test = testService.findByModuleId(moduleId);
        UserTest userTest = userTestRepository.findByUserIdAndTestId(userId, test.getId())
                .orElseThrow(() -> new EntityNotFoundException("userTest not found"));

        // 2. Test tugaganligini tekshiramiz
        boolean testFinished = userTest.isTestTimeOver();

        // 3. Savollar va javoblarni oldindan yuklaymiz
        List<Question> questions = questionRepository.findAllByTestIdAndIsDeletedFalseOrderById(test.getId());
        List<UserTestAnswer> answers = userTestAnswerRepository.findAllByUserTestIdAndIsDeletedFalse(userTest.getId());

        // 4. Javoblarni questionId bo‘yicha maplaymiz
        Map<Long, UserTestAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        a -> a.getQuestionOption().getQuestion().getId(),
                        a -> a,
                        (a1, a2) -> a1 // agar bir nechta bo‘lsa, birinchisini olamiz
                ));

        // 5. DTOlarni yaratamiz
        List<UserQuestionDto> userQuestionDtos = new ArrayList<>();
        int questionNumber = 1;
        for (Question question : questions) {
            UserTestAnswer answer = answerMap.get(question.getId());
            boolean chosen = answer != null;
            Boolean correct = testFinished && chosen ? answer.getQuestionOption().getIsCorrect() : null;

            userQuestionDtos.add(new UserQuestionDto(
                    question.getId(),
                    questionNumber++,
                    chosen,
                    correct
            ));
        }

        return userQuestionDtos;
    }

    public void answer(Long questionOptionId) {
        Long authUserId = authUserService.getAuthUserId();
        QuestionOption questionOption = questionOptionRepository.findById(questionOptionId).orElseThrow();
        UserTest userTest = userTestRepository.findByUserIdAndTestId(authUserId, questionOption.getQuestion().getTest().getId()).orElseThrow();
        if (userTest.isTestTimeOver()) {
            throw new CustomException("test time already was over !!!", HttpStatus.FORBIDDEN, "TEST_ALREADY_FINISHED");
        }
        UserTestAnswer userTestAnswer = userTestAnswerRepository.findByUserTestIdAndQuestionId(userTest.getId(), questionOption.getQuestion().getId()).orElseGet(() ->
                UserTestAnswer.builder()
                        .userTest(userTest)
                        .questionOption(questionOption)
                        .isDeleted(false)
                        .build());
        userTestAnswer.setQuestionOption(questionOption);
        userTestAnswerRepository.save(userTestAnswer);
    }

    /*public HttpEntity<?> getUserQuestion(Long questionId) {
        Long userId = authUserService.getAuthUserId();
        Question question = questionService.findQuestionById(questionId);
        List<QuestionOption> options = question.getOptions();
        UserTest userTest = userTestRepository.findByUserIdAndTestId(userId, question.getTest().getId()).orElseThrow();
        boolean testFinished = userTest.getStartedAt()
                .plusSeconds(userTest.getTest().getDurationSecond())
                .isBefore(LocalDateTime.now());
        List<UserTestAnswer> userTestAnswers = userTestAnswerRepository.findAllByUserTestIdAndIsDeletedFalse(userTest.getId());
        List<QuestionOptionUserRespDto> questionOptionUserRespDtos = new ArrayList<>();
        for (QuestionOption option : options) {
            Optional<UserTestAnswer> first = userTestAnswers.stream().filter(userTestAnswer -> userTestAnswer.getQuestionOption().getId().equals(option.getId())).findFirst();
            boolean chosen = first.isPresent();
            Boolean correct = testFinished && chosen ? first.get().getQuestionOption().getIsCorrect() : null;
            questionOptionUserRespDtos.add(new QuestionOptionUserRespDto(
                    option.getId(),
                    option.getResult(),
                    chosen,
                    correct
            ));
        }
        UserQuestionRespDto userQuestionRespDto = new UserQuestionRespDto(
                question.getId(),
                question.getQuestionName(),
                question.getDescription(),
                questionOptionUserRespDtos
        );
        return ResponseEntity.ok(userQuestionRespDto);
    }*/
    @Transactional(readOnly = true)
    public UserQuestionRespDto getUserQuestion(Long questionId) {
        Long userId = authUserService.getAuthUserId();
        Question question = questionService.findQuestionById(questionId);
        UserTest userTest = userTestRepository.findByUserIdAndTestId(userId, question.getTest().getId())
                .orElseThrow(() -> new EntityNotFoundException("userTest not found"));

        boolean testFinished = userTest.isTestTimeOver();

        // 1. Faqat kerakli savol uchun user javoblarini olamiz
        List<UserTestAnswer> userTestAnswers =
                userTestAnswerRepository.findAllByUserTestIdAndIsDeletedFalse(userTest.getId());

        // 2. Javoblarni questionOptionId bo‘yicha maplaymiz
        Map<Long, UserTestAnswer> answerMap = userTestAnswers.stream()
                .collect(Collectors.toMap(
                        a -> a.getQuestionOption().getId(),
                        a -> a,
                        (a1, a2) -> a1 // agar bir nechta bo‘lsa, birinchisini olamiz
                ));

        // 3. Variantlarni DTOga aylantiramiz
        List<QuestionOptionUserRespDto> optionDtos = question.getOptions().stream()
                .map(option -> {
                    UserTestAnswer userAnswer = answerMap.get(option.getId());
                    boolean chosen = userAnswer != null;
                    Boolean correct = testFinished && chosen ? option.getIsCorrect() : null;
                    return new QuestionOptionUserRespDto(
                            option.getId(),
                            option.getResult(),
                            chosen,
                            correct
                    );
                })
                .toList();

        // 4. Yakuniy DTOni yaratamiz

        return new UserQuestionRespDto(
                question.getId(),
                question.getQuestionName(),
                question.getDescription(),
                optionDtos
        );
    }

}
