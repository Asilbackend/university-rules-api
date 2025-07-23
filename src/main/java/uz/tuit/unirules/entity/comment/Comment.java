package uz.tuit.unirules.entity.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Comment extends BaseEntity {
    private String comment;
    @ManyToOne
    private Attachment attachment;
    @ManyToOne
    private User user;
}
