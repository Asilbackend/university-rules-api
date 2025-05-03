package uz.tuit.unirules.dto.request_dto.faculty;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CreateFacultyReqDto(
        @NotBlank
        String name,
        @NotBlank
        String description
) implements Serializable {
}
