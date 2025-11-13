package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record CreateTestReqDto(
        @NotNull Long moduleId,
        String title,
        String description,
        @NotNull
        Integer durationSecond
) implements Serializable {
}
