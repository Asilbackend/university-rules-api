package uz.tuit.unirules.entity.content;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ContentElementStudent extends BaseEntity {
    @ManyToOne
    private ContentElement contentElement;
    @ManyToOne
    private User student;
    @Builder.Default
    private Boolean isRead = false;
}
