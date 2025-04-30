package uz.tuit.unirules.mapper.faculty;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.faculty.FacultyRespDto;
import uz.tuit.unirules.entity.faculty.Faculty;

@Mapper(componentModel = "spring")
public interface FacultyMapper{
    FacultyRespDto toDto(Faculty faculty);
}
