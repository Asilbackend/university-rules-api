package uz.tuit.unirules.entity.test;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.modul.Module;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Test extends BaseEntity {
    @ManyToOne
    private Module module;
    private String title;
    private String description;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne
    private Content content;
    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
