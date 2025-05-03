package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UpdateRecommendedModuleReqDto(
        @NotBlank
        String reason,
        @NotBlank
        Long userId,
        @NotBlank
        Long moduleId
) implements Serializable {
}
