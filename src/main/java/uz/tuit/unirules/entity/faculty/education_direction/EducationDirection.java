package uz.tuit.unirules.entity.faculty.education_direction;

import jakarta.persistence.*;
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
    @Column(unique = true)
    private String name;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne
    private Faculty faculty;
   /* // qoshildi,
    @Enumerated(EnumType.STRING)
    private EducationType educationType;

    public enum EducationType {
        Kunduzgi, Sirtqi, Kechki, Masofaviy
    }*/
}
