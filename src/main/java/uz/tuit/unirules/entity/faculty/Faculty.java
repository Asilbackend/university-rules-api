package uz.tuit.unirules.entity.faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Faculty extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @Builder.Default
    private Boolean isDeleted = false;
}
