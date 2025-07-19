package uz.tuit.unirules.entity.user_test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.entity.user.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class UserTest extends BaseEntity {
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Test test;
    @Builder.Default
    private Boolean success = null;// null bolsa hali testni boshlamagan

    private Float result;
    @Column(nullable = false)
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    @Builder.Default
    private Boolean isDeleted = false;
}
