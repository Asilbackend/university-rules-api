package uz.tuit.unirules.dto.respond_dto;

import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content_student.ContentStudent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentRespDto(
        Long contentId,
        ContentStudent.UserContentStatus userContentStatus,
        String title,
        String type,
        String videoDuration
) implements Serializable {
}