package uz.tuit.unirules.dto.request_dto;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;

import java.io.Serializable;

public record CreateRecommendedModuleReqDto(
        @NotBlank
        String reason,
        @NotBlank
        Long userId,
        @NotBlank
        Long moduleId
) implements Serializable {
}
