package uz.tuit.unirules.entity.notification;

import jakarta.persistence.*;
import lombok.*;
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
    private User user;
    private String title;
    private String message;
    Boolean is_read;
}
