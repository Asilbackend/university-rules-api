package uz.tuit.unirules.dto.request_dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link uz.tuit.unirules.entity.attachment.Attachment}
 */
public record AttachmentCreateDto(
        Timestamp createdAt, String fileName
) implements Serializable {
}