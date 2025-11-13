package uz.tuit.unirules.entity.user_image;

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
public class UserImage extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private Attachment attachment;
    private Boolean deleted=false;
}
