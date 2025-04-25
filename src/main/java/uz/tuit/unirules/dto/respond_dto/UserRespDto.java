package uz.tuit.unirules.dto.respond_dto;

import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.faculty.group.Group;

public record UserRespDto(
        String firstname,
        String lastname,
       //todo: String username,
        String email,
        String phone,
        String language,
        boolean passedTest,
        Group group,
        Role role
) {
}
