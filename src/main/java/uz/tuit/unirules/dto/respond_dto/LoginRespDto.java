package uz.tuit.unirules.dto.respond_dto;

public record LoginRespDto(
        String accessToken,
        String refreshToken
) {

}
