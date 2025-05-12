package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UpdateDisciplineRuleReqDto(
        @NotBlank
        String title,
        @NotBlank
        String body,
        Long attachmentId
) implements Serializable {
}
