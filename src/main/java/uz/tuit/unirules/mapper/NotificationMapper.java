package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tuit.unirules.dto.respond_dto.NotificationRespDto;
import uz.tuit.unirules.entity.notification.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "user.id",target = "userId")
    NotificationRespDto toDto(Notification notification);
}
