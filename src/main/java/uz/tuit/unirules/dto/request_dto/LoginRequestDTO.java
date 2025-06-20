package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record LoginRequestDTO(
        @NotBlank(message = "login is mandatory")  // Email bo'sh bo'lmasligi kerak
        //  @Email(message = "Invalid email format")   // Email noto'g'ri formatda bo'lsa
        String login,
        @NotBlank(message = "login is mandatory")
        String password
) implements Serializable {
}