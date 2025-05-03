package uz.tuit.unirules.entity.test;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Question extends BaseEntity {
    private String questionName;
    private String description;
    @Builder.Default
    private Boolean isDeleted = false;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;
    @ManyToOne
    @JoinColumn(name = "test")
    private Test test;
}
