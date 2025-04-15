package uz.tuit.unirules.entity.user_test;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class UserTest extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private Test test;
    private Boolean success;
    @Builder.Default
    private Integer tryCount = 0;// urinishlar soni

}
