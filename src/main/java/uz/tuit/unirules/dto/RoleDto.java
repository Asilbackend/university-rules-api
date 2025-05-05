package uz.tuit.unirules.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record RoleDto(
        @NotBlank
        String role
) implements Serializable {
}
