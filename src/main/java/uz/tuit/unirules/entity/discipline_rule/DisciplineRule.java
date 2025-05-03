package uz.tuit.unirules.entity.discipline_rule;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String body;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    private Attachment attachment;
}
