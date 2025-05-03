package uz.tuit.unirules.entity.faculty.education_direction;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.faculty.Faculty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class EducationDirection extends BaseEntity {
    private String name;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne
    private Faculty faculty;
}
