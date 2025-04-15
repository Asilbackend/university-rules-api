package uz.tuit.unirules.entity.attachment_rating;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class AttachmentRating extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private Attachment attachment;
    @Max(5)
    @Min(0)
    private Integer rating;
    private String comment;
}
