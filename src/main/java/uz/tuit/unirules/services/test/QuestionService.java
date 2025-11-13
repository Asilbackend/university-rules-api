package uz.tuit.unirules.services.test;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateQuestionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionOptionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionReqDto;
import uz.tuit.unirules.dto.respond_dto.QuestionOptionRespDto;
import uz.tuit.unirules.dto.respond_dto.QuestionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.mapper.test.QuestionMapper;
import uz.tuit.unirules.repository.test.QuestionOptionRepository;
import uz.tuit.unirules.repository.test.QuestionRepository;

import java.util.List;
import java.util.Map;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TestService testService;

    private final QuestionOptionService questionOptionService;

    public QuestionService(QuestionRepository questionRepository, TestService testService, QuestionOptionService questionOptionService) {
        this.questionRepository = questionRepository;
        this.testService = testService;
        this.questionOptionService = questionOptionService;
    }

    @Transactional
    public ResponseEntity<?> create(CreateQuestionReqDto createReqDto) {
        long countTrue = createReqDto.createQuestionOptionReqDtos().stream().filter(q -> q.isCorrect().equals(true)).count();
        if (countTrue != 1) {
            throw new RuntimeException("number of correct answer must be one but now n= " + countTrue);
        }
        Test test = testService.findByTestId(createReqDto.testId());
        Question question = Question.builder()
                .questionName(createReqDto.questionName())
                .description(createReqDto.description())
                .test(test)
                .build();
        Question save = questionRepository.save(question);
        questionOptionService.create(createReqDto.createQuestionOptionReqDtos(), save);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("questionId", save.getId()));
    }

    @Transactional(readOnly = true)
    public ApiResponse<QuestionRespDto> get(Long entityId) {
        QuestionRespDto questionRespDto = getQuestionRespDto(entityId);
        return new ApiResponse<>(
                200,
                "found",
                true,
                questionRespDto
        );
    }

    private QuestionRespDto getQuestionRespDto(Long questionId) {
        Question question = findQuestionById(questionId);
        List<QuestionOptionRespDto> optionRespDtos = question.getOptions().stream()
                .map(option -> new QuestionOptionRespDto(option.getId(), option.getResult()))
                .toList();
        return new QuestionRespDto(
                question.getId(),
                question.getQuestionName(),
                question.getDescription(),
                optionRespDtos
        );
    }


    public Question findQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException
                        (" question is not found by this id = %s".formatted(questionId)));
        if (Boolean.TRUE.equals(question.getIsDeleted())) {
            throw new EntityNotFoundException("Question with id = %s is deleted".formatted(questionId));
        }
        return question;
    }

    @Transactional
    public ApiResponse<QuestionRespDto> update(Long id, UpdateQuestionReqDto updateDto) {
        // ✅ 1. Faqat bitta to‘g‘ri javob bo‘lishi kerakligini tekshirish
        long correctCount = updateDto.updateQuestionOptionReqDtos()
                .stream()
                .filter(UpdateQuestionOptionReqDto::isCorrect)
                .count();

        if (correctCount != 1) {
            throw new IllegalArgumentException(
                    "Exactly one correct answer is required, but found: " + correctCount
            );
        }

        // ✅ 2. Questionni olish va ma'lumotlarini yangilash
        Question question = findQuestionById(id);
        question.setQuestionName(updateDto.questionName());
        question.setDescription(updateDto.description());

        // ✅ 3. Variantlarni yangilashni chaqirish (faqat shu joyda bitta save bo‘ladi)
        questionOptionService.updateOptions(question, updateDto.updateQuestionOptionReqDtos());
        return new ApiResponse<>(200, "Question updated successfully", true, null);
    }


    @Transactional
    public ApiResponse<QuestionRespDto> delete(Long id) {
        Question question = findQuestionById(id);
        question.setIsDeleted(true);
        questionRepository.save(question);
        return new ApiResponse<>(
                200,
                "deleted",
                true,
                null
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<QuestionRespDto>> getAll() {
        List<Question> questions = questionRepository.findAllByIsDeletedFalse();
        List<QuestionRespDto> dto = questions.stream().map(question -> getQuestionRespDto(question.getId())).toList();
        return new ApiResponse<>(
                200,
                "all questions",
                true,
                dto
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<QuestionRespDto>> getAllPagination(Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByIsDeletedFalse(pageable);
        Page<QuestionRespDto> dto = questions.map(question -> getQuestionRespDto(question.getId()));
        return new ApiResponse<>(
                200,
                "all questions",
                true,
                dto
        );
    }
}
