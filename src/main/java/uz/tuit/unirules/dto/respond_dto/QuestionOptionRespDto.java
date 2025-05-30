package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;

public record QuestionOptionRespDto (
        Long questionOptionId,
        Long questionId,
        String result

) implements Serializable {
}
