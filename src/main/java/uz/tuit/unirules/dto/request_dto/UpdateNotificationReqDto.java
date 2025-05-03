package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record UpdateNotificationReqDto(
        @NotBlank
        String title,
        @NotBlank
        String message,
        Boolean is_read,
        @NotBlank
        Long userId
) implements Serializable {
}
