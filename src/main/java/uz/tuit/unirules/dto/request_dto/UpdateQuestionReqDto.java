package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record UpdateQuestionReqDto(
        @NotBlank
        String questionName,
        String description,
        List<UpdateQuestionOptionReqDto> updateQuestionOptionReqDtos
) implements Serializable {
}
