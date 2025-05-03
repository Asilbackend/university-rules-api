package uz.tuit.unirules.dto.respond_dto.faculty;

import java.io.Serializable;

public record GroupRespDto(
        String name,
        Long educationDirectionId
) implements Serializable {
}
