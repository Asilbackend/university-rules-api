package uz.tuit.unirules.mapper.faculty;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tuit.unirules.dto.respond_dto.faculty.GroupRespDto;
import uz.tuit.unirules.entity.faculty.group.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "educationDirection.id",target = "educationDirectionId")
    GroupRespDto toDto(Group group);
}
