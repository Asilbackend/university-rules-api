package uz.tuit.unirules.dto.respond_dto;


import java.io.Serializable;

public record UserRespDto(
        Long userId,
        String firstname,
        String lastname,
        //todo: String username,
        String email,
        String phone,
        String language,
        boolean passedTest,
        Long groupId,
        String role
) implements Serializable {
}
