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
    @Builder.Default
    private Integer tryCount = 0;// nechanchi urunishdagi answer?
    @ManyToOne(optional = false)
    private UserTest userTest;
    @ManyToOne
    private QuestionOption questionOption;
    @Builder.Default
    private Boolean isDeleted = false;
}
