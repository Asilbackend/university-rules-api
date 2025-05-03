package uz.tuit.unirules.dto.request_dto;

import java.io.Serializable;

public record CreateUserReqDto(
        String firstname,
        String lastname,
        String username,
        String password,
        String rePassword,
        String email,
        String phone,
        String language,
        Long groupId,
        String role
) implements Serializable {
}
