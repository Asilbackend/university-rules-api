package uz.tuit.unirules.dto.request_dto.faculty;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CreateGroupReqDto(
        @NotBlank
        String name,
        @NotBlank
        Long educationDirectionId
) implements Serializable {
}
