package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentCreateDto(@NotNull Long moduleId,
                               String contentTitle,
                               Map<String, String> titlesAndBodies,
                               Map<Long, String> attachmentsAndTitles

) implements Serializable {

}