package uz.tuit.unirules.entity.user_test;

import jakarta.persistence.ManyToOne;
import lombok.Builder;
import uz.tuit.unirules.entity.abs.BaseEntity;
import jakarta.persistence.Entity;

import lombok.*;
import uz.tuit.unirules.entity.test.QuestionOption;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class UserTestAnswer extends BaseEntity {
    // user testni necha marta ishlasa oshancha marta shu entity yaratiladi!!!!!!!!!
    @ManyToOne(optional = false)
    private UserTest userTest;
    @ManyToOne
    private QuestionOption questionOption;
    @Builder.Default
    private Boolean isDeleted = false; // hozrcha ishlatma
}
