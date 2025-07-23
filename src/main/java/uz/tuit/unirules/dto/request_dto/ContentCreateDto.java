package uz.tuit.unirules.dto.request_dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link uz.tuit.unirules.entity.content.Content}
 */
public record ContentCreateDto(@NotNull Long moduleId,
                               @NotNull String contentTitle,
                               @NotNull Boolean required,
                               @NotNull List<TextElement> textElements,
                               @NotNull List<AttachmentElement> attachmentElements

) implements Serializable {

    public record TextElement(
            String title,
            String body,
            Integer orderElement
    ) {
    }

    public record AttachmentElement(
            String title,
            Long attachmentId,
            Integer orderElement

    ) {
    }
}