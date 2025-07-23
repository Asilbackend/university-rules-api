package uz.tuit.unirules.dto.request_dto;

import java.io.Serializable;

public record NewsRequestDto(
        String name,
        String description,
        Long attachmentId
) implements Serializable {
}
