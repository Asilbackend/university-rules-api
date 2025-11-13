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
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;
import uz.tuit.unirules.mapper.test.QuestionOptionMapper;
import uz.tuit.unirules.repository.test.QuestionOptionRepository;
import uz.tuit.unirules.repository.test.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionOptionService {
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;

    public QuestionOptionService(QuestionOptionRepository questionOptionRepository,
                                 QuestionRepository questionRepository) {
        this.questionOptionRepository = questionOptionRepository;
        this.questionRepository = questionRepository;
    }


    public QuestionOption findQuestionOptionById(Long questionOptionId) {
        return questionOptionRepository.findById(questionOptionId).orElseThrow(
                () -> new EntityNotFoundException("Question Option is not found by this id = %s".formatted(questionOptionId))
        );
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


    public void create(List<CreateQuestionOptionReqDto> questionOptionReqDtos, Question question) {
        for (CreateQuestionOptionReqDto dto : questionOptionReqDtos) {
            QuestionOption questionOption = QuestionOption.builder()
                    .question(question)
                    .isCorrect(dto.isCorrect())
                    .result(dto.result())
                    .isDeleted(false)
                    .build();
            questionOptionRepository.save(questionOption);
        }
    }

    /*public void update(List<UpdateQuestionOptionReqDto> updateQuestionOptionReqDtos, Question question) {
        List<UpdateQuestionOptionReqDto> newOptions = updateQuestionOptionReqDtos.stream().filter(o -> o.questionOptionId() == null).toList();
        List<QuestionOption> questionOptions = new ArrayList<>();//all
        for (UpdateQuestionOptionReqDto newOption : newOptions) {
            QuestionOption questionOption = QuestionOption.builder()
                    .question(question)
                    .isCorrect(newOption.isCorrect())
                    .result(newOption.result())
                    .isDeleted(false)
                    .build();
            questionOptions.add(questionOptionRepository.save(questionOption));
        }
        List<UpdateQuestionOptionReqDto> oldOptions = updateQuestionOptionReqDtos.stream().filter(o -> o.questionOptionId() != null).toList();
        for (UpdateQuestionOptionReqDto oldOption : oldOptions) {
            Long questionOptionId = oldOption.questionOptionId();
            QuestionOption questionOption = findQuestionOptionById(questionOptionId);
            questionOption.setIsCorrect(oldOption.isCorrect());
            questionOption.setResult(oldOption.result());
            questionOptions.add(questionOptionRepository.save(questionOption));
        }
        question.setOptions(questionOptions);
        questionRepository.save(question);
    }*/

    @Transactional
    public void updateOptions(Question question, List<UpdateQuestionOptionReqDto> optionDtos) {
        // Eski optionlar
        List<QuestionOption> options = question.getOptions();
        Map<Long, QuestionOption> existingOptions = options
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        List<QuestionOption> updatedOptions = new ArrayList<>();

        for (UpdateQuestionOptionReqDto dto : optionDtos) {
            QuestionOption option;

            if (dto.questionOptionId() != null) {
                // ✅ Eski variantni yangilaymiz
                option = existingOptions.get(dto.questionOptionId());
                if (option == null) {
                    throw new IllegalArgumentException("Question option not found: " + dto.questionOptionId());
                }
                option.setIsCorrect(dto.isCorrect());
                option.setResult(dto.result());
            } else {
                // ✅ Yangi variant qo‘shamiz (save kerak emas, cascade ALL qiladi)
                option = QuestionOption.builder()
                        .question(question)
                        .isCorrect(dto.isCorrect())
                        .result(dto.result())
                        .isDeleted(false)
                        .build();
            }

            updatedOptions.add(option);
        }

        // ✅ OrphanRemoval ishlaydi — eski variantlardan qolganlari avtomatik o‘chiriladi

        options.clear();
        options.addAll(updatedOptions);
    }


}
