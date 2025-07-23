package uz.tuit.unirules.dto.respond_dto;

import lombok.Builder;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.modul.Module;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentRespDto(
        Long contentId,
        String title,
        Module module,// module majburiy bolsa content ham majburiy
        Boolean isRequired,
        Double averageContentRating,
        Boolean isDeleted
) implements Serializable {
}