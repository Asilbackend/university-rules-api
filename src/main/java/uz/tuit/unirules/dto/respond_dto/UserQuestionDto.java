package uz.tuit.unirules.dto.respond_dto;

public record UserQuestionDto(
        Long questionId,
        Integer questionNumber,
        Boolean chosen,
        Boolean correct
) {

}
