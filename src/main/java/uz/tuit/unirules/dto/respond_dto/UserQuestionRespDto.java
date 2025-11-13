package uz.tuit.unirules.dto.respond_dto;

import java.util.List;

public record UserQuestionRespDto(
        Long questionId,
        String questionName,
        String description,
        List<QuestionOptionUserRespDto> options
) {

}
