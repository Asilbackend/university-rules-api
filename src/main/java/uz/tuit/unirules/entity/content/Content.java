package uz.tuit.unirules.entity.content;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.modul.Module;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Content extends BaseEntity {
    private String title;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentElement> contentElements;
    @ManyToOne
    private Module module;// module majburiy bolsa content ham majburiy
    @Builder.Default
    private Boolean isRequired = null;
    @Builder.Default
    private Double averageContentRating=0D;
    @Builder.Default
    private Boolean isDeleted = false;

}
