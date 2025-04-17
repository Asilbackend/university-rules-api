package uz.tuit.unirules.entity.faculty.group;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group extends BaseEntity {
    private String name;
    @ManyToOne
    private EducationDirection educationDirection;
}
