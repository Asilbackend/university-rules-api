package uz.tuit.unirules.dto.respond_dto;


import java.io.Serializable;

public record DisciplineRuleRespDto(
        Long id,
        String title,
        String body,
        Long attachmentId
) implements Serializable {
}
