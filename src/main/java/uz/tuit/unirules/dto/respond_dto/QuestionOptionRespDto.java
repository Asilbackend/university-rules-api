package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;

public record QuestionOptionRespDto(
        Long questionOptionId,
        String result

) implements Serializable {
}
