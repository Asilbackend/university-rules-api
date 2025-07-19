package uz.tuit.unirules.entity.notification;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Notification extends BaseEntity {
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User user;
    private String title;
    private String message;
    private Boolean is_read;
    @Builder.Default
    private Boolean isDeleted = false;
}
