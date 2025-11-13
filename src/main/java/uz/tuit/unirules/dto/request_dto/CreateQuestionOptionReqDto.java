package uz.tuit.unirules.dto.request_dto;


import java.io.Serializable;

public record CreateQuestionOptionReqDto(
         /*Long questionId,*/
         String result,
         Boolean isCorrect
) implements Serializable {

}
