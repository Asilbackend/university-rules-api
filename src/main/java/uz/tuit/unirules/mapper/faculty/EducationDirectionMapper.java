package uz.tuit.unirules.mapper.faculty;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tuit.unirules.dto.respond_dto.faculty.EducationDirectionRespDto;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;

@Mapper(componentModel = "spring")
public interface EducationDirectionMapper {
    @Mapping(source = "faculty.id",target = "facultyId")
    EducationDirectionRespDto toDto(EducationDirection educationDirection);
}
