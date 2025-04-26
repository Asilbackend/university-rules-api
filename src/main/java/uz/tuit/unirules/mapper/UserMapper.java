package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.UserRespDto;
import uz.tuit.unirules.entity.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRespDto toRespDto(User user);

}
