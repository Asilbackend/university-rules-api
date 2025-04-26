package uz.tuit.unirules.dto.request_dto;

public record UpdateUserReqDto(
        String firstname,
        String lastname,
        String email,
        String phone
) {

}
