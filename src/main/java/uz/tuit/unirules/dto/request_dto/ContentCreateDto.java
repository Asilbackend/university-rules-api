package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentCreateDto(@NotNull @NotEmpty @NotBlank String title, String body, Long moduleId,
                               Boolean isRequired,
                               Double averageContentRating, List<Long> attachmentIds) implements Serializable {
}