package uz.tuit.unirules.entity.faculty;

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
    private String name;
    private String description;
    @Builder.Default
    private Boolean isDeleted = false;
}
