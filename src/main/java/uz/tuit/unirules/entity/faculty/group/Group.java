package uz.tuit.unirules.entity.faculty.group;

import jakarta.persistence.Column;
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
    @Column(unique = true, nullable = false)
    private String name;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne
    private EducationDirection educationDirection;
}
