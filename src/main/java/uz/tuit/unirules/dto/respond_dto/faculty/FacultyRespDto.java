package uz.tuit.unirules.dto.respond_dto.faculty;

import java.io.Serializable;

public record FacultyRespDto(
         String name,
         String description
) implements Serializable {
}
