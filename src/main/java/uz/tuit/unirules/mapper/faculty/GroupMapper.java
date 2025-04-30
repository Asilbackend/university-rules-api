package uz.tuit.unirules.mapper.faculty;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.faculty.GroupRespDto;
import uz.tuit.unirules.entity.faculty.group.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupRespDto toDto(Group group);
}
