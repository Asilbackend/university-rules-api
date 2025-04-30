package uz.tuit.unirules.mapper.faculty;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.faculty.EducationDirectionRespDto;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;

@Mapper(componentModel = "spring")
public interface EducationDirectionMapper {
    EducationDirectionRespDto toDto(EducationDirection educationDirection);
}
