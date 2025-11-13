package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UpdateQuestionOptionReqDto(
        Long questionOptionId, // agar null bolsa bu yangi QuestionOptiondir deb qabul qilamiz
        @NotNull
        @NotEmpty
        String result,
        @NotNull
        Boolean isCorrect

) implements Serializable {
}
