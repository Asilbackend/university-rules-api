package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;
import java.util.List;

public record QuestionRespDto(
        Long questionId,
        String questionName,
        String description,
        List<Long> optionIds,
        Long testId
) implements Serializable {
}
