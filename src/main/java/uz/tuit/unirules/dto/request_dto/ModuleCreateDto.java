package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import uz.tuit.unirules.entity.modul.Module;

import java.io.Serializable;

/**
 * DTO for {@link uz.tuit.unirules.entity.modul.Module}
 */
public record ModuleCreateDto(@NotEmpty @NotBlank String name, String description,
                              @NotNull Module.ModuleState moduleState) implements Serializable {
}