package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UpdateQuestionOptionReqDto(
        @NotNull
        Long questionOptionId,
        @NotNull
        Long questionId,
        @NotNull
        @NotEmpty
        String result,
        @NotNull
        Boolean isCorrect

) implements Serializable {
}
