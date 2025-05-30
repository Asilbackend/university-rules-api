package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.tuit.unirules.entity.test.Test;
import java.io.Serializable;
import java.util.List;

public record CreateQuestionReqDto(
        @NotBlank // null yoki "" ni oldini olish
        String questionName,
        @NotBlank
        String description,
        Long testId
)implements Serializable {
}
