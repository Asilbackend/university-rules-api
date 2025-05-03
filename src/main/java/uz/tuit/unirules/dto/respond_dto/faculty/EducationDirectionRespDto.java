package uz.tuit.unirules.dto.respond_dto.faculty;

import java.io.Serializable;

public record EducationDirectionRespDto(
        String name,
        Long facultyId

) implements Serializable {
}
