package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record UpdateQuestionReqDto(
        Long questionId,
        @NotBlank
        String questionName,
        @NotNull
        String description,
        Long testId
) implements Serializable {
}
