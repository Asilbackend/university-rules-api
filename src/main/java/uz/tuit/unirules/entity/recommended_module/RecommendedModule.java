package uz.tuit.unirules.entity.recommended_module;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class RecommendedModule extends BaseEntity {
    @ManyToOne(optional = true)
    private User user; // user null bolsa hammaga tavsiya etilgan boladi!!!!!
    @ManyToOne
    private Module module;
    private String reason;
    @Builder.Default
    private Boolean isDeleted = false;
}
