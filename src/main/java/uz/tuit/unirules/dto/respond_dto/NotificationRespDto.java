package uz.tuit.unirules.dto.respond_dto;

import uz.tuit.unirules.entity.user.User;

import java.io.Serializable;

public record NotificationRespDto(
        Long userId,
        String title,
        String message,
        Boolean is_read
) implements Serializable {
}
