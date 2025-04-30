package uz.tuit.unirules.dto.respond_dto;

import uz.tuit.unirules.entity.user.User;

public record NotificationRespDto(
        User user,
        String title,
        String message,
        Boolean is_read
) {
}
