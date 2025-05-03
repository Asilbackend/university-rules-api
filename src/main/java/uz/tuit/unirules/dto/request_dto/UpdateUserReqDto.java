package uz.tuit.unirules.dto.request_dto;

import java.io.Serializable;

public record UpdateUserReqDto(
        String firstname,
        String lastname,
        String email,
        String phone,
        Long groupId,
        String role
) implements Serializable {

}
