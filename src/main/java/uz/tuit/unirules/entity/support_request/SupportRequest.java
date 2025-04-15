package uz.tuit.unirules.entity.support_request;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class SupportRequest extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private User supportUser;
    private String subject;
    private String message;
    @Enumerated(value = EnumType.STRING)
    private Status status=Status.CLOSED;

    public enum Status {
        OPEN, IN_PROGRESS, CLOSED
    }
}
