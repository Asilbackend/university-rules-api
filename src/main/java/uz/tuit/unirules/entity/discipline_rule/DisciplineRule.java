package uz.tuit.unirules.entity.discipline_rule;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class DisciplineRule extends BaseEntity {
    private String title;
    private String description;
    @ManyToOne
    private Attachment attachment;
}
