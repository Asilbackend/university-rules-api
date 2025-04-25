package uz.tuit.unirules.dto.request_dto;

import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.faculty.group.Group;

public record UserReqDto(
        String firstname,
        String lastname,
        String username,
        String password,
        String email,
        String phone,
        String language
) {
}
