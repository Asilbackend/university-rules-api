package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.tuit.unirules.entity.user.User;

public record CreateNotificationReqDto(
        @NotBlank
        String title,
        @NotBlank
        String message,
        Boolean is_read,
        @NotBlank
        Long userId
) {
}
