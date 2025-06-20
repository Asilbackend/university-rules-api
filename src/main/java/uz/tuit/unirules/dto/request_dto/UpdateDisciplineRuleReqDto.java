package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UpdateDisciplineRuleReqDto(
        @NotBlank
        String title,
        @NotBlank
        String body,
        @NotNull
        Long attachmentId
) implements Serializable {
}
