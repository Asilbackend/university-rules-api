package uz.tuit.unirules.dto.request_dto;

public record CreateUserReqDto(
        String firstname,
        String lastname,
        String username,
        String password,
        String rePassword,
        String email,
        String phone,
        String language
) {
}
