package uz.tuit.unirules.dto.respond_dto;


import java.io.Serializable;

public record RecommendedModuleRespDto(
        String reason,
        Long userId,
        Long moduleId
) implements Serializable {
}
