package uz.tuit.unirules.dto.respond_dto;

public record QuestionOptionUserRespDto(
        Long questionOptionId,
        String result,
        Boolean chosen,
        Boolean correct
) {
}
