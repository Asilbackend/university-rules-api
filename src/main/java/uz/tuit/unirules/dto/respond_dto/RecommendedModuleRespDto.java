package uz.tuit.unirules.dto.respond_dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record RecommendedModuleRespDto(
        String reason,
        Long userId,
        Long moduleId
) implements Serializable {
}
