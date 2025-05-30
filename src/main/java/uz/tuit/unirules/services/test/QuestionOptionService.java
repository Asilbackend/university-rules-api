package uz.tuit.unirules.services.test;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateQuestionOptionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionOptionReqDto;
import uz.tuit.unirules.dto.respond_dto.QuestionOptionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;
import uz.tuit.unirules.mapper.test.QuestionOptionMapper;
import uz.tuit.unirules.repository.test.QuestionOptionRepository;

import java.util.List;

@Service
public class QuestionOptionService {
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionService questionService;
    private final QuestionOptionMapper questionOptionMapper;

    public QuestionOptionService(QuestionOptionRepository questionOptionRepository, QuestionService questionService, QuestionOptionMapper questionOptionMapper) {
        this.questionOptionRepository = questionOptionRepository;
        this.questionService = questionService;
        this.questionOptionMapper = questionOptionMapper;
    }

    @Transactional
    public ApiResponse<QuestionOptionRespDto> create(CreateQuestionOptionReqDto reqDto) {
        // question ni topish
        Question question = questionService.findQuestionById(reqDto.questionId());

        QuestionOption questionOption = QuestionOption.builder()
                .question(question)
                .isCorrect(reqDto.isCorrect())
                .result(reqDto.result())
                .build();

        questionOptionRepository.save(questionOption);

        QuestionOptionRespDto respDto = new QuestionOptionRespDto(
                questionOption.getId(),
                question.getId(),
                questionOption.getResult()
        );
        return new ApiResponse<>(
                201,
                "saved",
                true,
                respDto
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<QuestionOptionRespDto> get(Long entityId) {
        QuestionOption questionOption = findQuestionOptionById(entityId);
        QuestionOptionRespDto respDto = new QuestionOptionRespDto(
                questionOption.getId(),
                questionOption.getQuestion().getId(),
                questionOption.getResult()
        );
        return new ApiResponse<>(
                200,
                "found",
                true,
                respDto
        );
    }

    public QuestionOption findQuestionOptionById(Long questionOptionId) {
        return questionOptionRepository.findById(questionOptionId).orElseThrow(
                () -> new EntityNotFoundException("Question Option is not found by this id = %s".formatted(questionOptionId))
        );
    }

    @Transactional
    public ApiResponse<QuestionOptionRespDto> update(Long id, UpdateQuestionOptionReqDto reqDto) {
        QuestionOption option = findQuestionOptionById(id);
        Question question = questionService.findQuestionById(reqDto.questionId());
        option.setQuestion(question);
        option.setResult(reqDto.result());
        option.setIsCorrect(reqDto.isCorrect());
        questionOptionRepository.save(option);
        return new ApiResponse<>(201, "updated", true, null);
    }

    @Transactional
    public ApiResponse<QuestionOptionRespDto> delete(Long id) {
        QuestionOption option = findQuestionOptionById(id);
        option.setIsDeleted(true);
        questionOptionRepository.save(option);
        return new ApiResponse<>(
                200,
                "deleted",
                true,
                null
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<QuestionOptionRespDto>> getAll() {
        List<QuestionOption> optionList = questionOptionRepository.findAllByIsDeletedFalse();
        List<QuestionOptionRespDto> dtoList = optionList.stream()
                .map(questionOptionMapper::respDto).toList();
        return new ApiResponse<>(
                200,
                "all options",
                true,
                dtoList
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<QuestionOptionRespDto>> getAllPagination(Pageable pageable) {
        Page<QuestionOption> optionPage = questionOptionRepository.findAllByIsDeletedFalse(pageable);
        Page<QuestionOptionRespDto> dto = optionPage.map(questionOptionMapper::respDto);
        return new ApiResponse<>(
                200,
                "all options",
                true,
                dto
        );
    }
}
