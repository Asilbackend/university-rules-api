package uz.tuit.unirules.dto.request_dto.faculty;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CreateEducationDirectionReqDto(
        @NotBlank
        String name,
        @NotBlank
        Long facultyId

) implements Serializable {
}
