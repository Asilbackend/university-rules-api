package uz.tuit.unirules.entity.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class QuestionOption extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private String result;
    @JsonIgnore
    private Boolean isCorrect;
    @Builder.Default
    private Boolean isDeleted = false;
}