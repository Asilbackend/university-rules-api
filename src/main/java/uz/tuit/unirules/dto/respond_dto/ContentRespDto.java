package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentRespDto(Long id, String title, String body, List<Long> attachmentIds, Long moduleId,
                             Double averageContentRating) implements Serializable {
}