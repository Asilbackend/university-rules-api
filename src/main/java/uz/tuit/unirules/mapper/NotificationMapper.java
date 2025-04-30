package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.NotificationRespDto;
import uz.tuit.unirules.entity.notification.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationRespDto toDto(Notification notification);
}
