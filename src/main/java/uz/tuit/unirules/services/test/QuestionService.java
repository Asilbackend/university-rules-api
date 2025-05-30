package uz.tuit.unirules.services.test;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateQuestionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionReqDto;
import uz.tuit.unirules.dto.respond_dto.QuestionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.mapper.test.QuestionMapper;
import uz.tuit.unirules.repository.test.QuestionOptionRepository;
import uz.tuit.unirules.repository.test.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TestService testService;
    private final QuestionMapper questionMapper;
    private final QuestionOptionRepository questionOptionRepository;

    public QuestionService(QuestionRepository questionRepository, TestService testService, QuestionMapper questionMapper, QuestionOptionRepository questionOptionRepository) {
        this.questionRepository = questionRepository;
        this.testService = testService;
        this.questionMapper = questionMapper;
        this.questionOptionRepository = questionOptionRepository;
    }

    @Transactional
    public ApiResponse<QuestionRespDto> create(CreateQuestionReqDto createReqDto) {
        Test test = testService.findByTestId(createReqDto.testId());
        Question question = Question.builder()
                .questionName(createReqDto.questionName())
                .description(createReqDto.description())
                .test(test)
                .build();
        questionRepository.save(question);
        QuestionRespDto dto = questionMapper.toRespDto(question);
        return new ApiResponse<>(
                201,
                "saved",
                true,
                dto
        );
    }

    public List<QuestionOption> findQuestionOptionByIds(List<Long> optionIds) {
        return optionIds == null || optionIds.isEmpty() ?
                List.of() :
                questionOptionRepository.findAllById(optionIds);
    }

    @Transactional(readOnly = true)
    public ApiResponse<QuestionRespDto> get(Long entityId) {
        Question question = findQuestionById(entityId);
        QuestionRespDto dto = questionMapper.toRespDto(question);
        return new ApiResponse<>(
                200,
                "found",
                true,
                dto
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
        Test test = testService.findByTestId(updateDto.testId());
        Question question = findQuestionById(id);
        question.setQuestionName(updateDto.questionName());
        question.setTest(test);
        question.setDescription(updateDto.description());
        questionRepository.save(question);
        return new ApiResponse<>(
                200,
                "updated",
                true,
                null
        );
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
        List<QuestionRespDto> dto = questions.stream().map(questionMapper::toRespDto).toList();
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
        Page<QuestionRespDto> dto = questions.map(questionMapper::toRespDto);
        return new ApiResponse<>(
                200,
                "all questions",
                true,
                dto
        );
    }
}
