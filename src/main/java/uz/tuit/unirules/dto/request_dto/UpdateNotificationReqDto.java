package uz.tuit.unirules.dto.request_dto;

import uz.tuit.unirules.entity.user.User;

public record UpdateNotificationReqDto(
        User user,
        String title,
        String message,
        Boolean is_read
) {
}
