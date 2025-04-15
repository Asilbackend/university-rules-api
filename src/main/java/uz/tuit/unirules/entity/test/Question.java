package uz.tuit.unirules.entity.test;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
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
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;
    @ManyToOne
    @JoinColumn(name = "test")
    private Test test;
}
