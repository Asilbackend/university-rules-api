package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public record UpdateTestReqDto(
        Long moduleId, @NotBlank String title, @NotBlank String description
) implements Serializable {
}
