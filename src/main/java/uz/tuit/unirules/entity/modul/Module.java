package uz.tuit.unirules.entity.modul;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Module extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    private ModuleState moduleState = ModuleState.OPTIONAL; /// hamma uchun majburiy yoki majburiy emas yoki umuman korsatmaslik
    @Builder.Default
    private Boolean isDeleted = false;

    public enum ModuleState {
        REQUIRED, OPTIONAL, INVISIBLE
    }
}
