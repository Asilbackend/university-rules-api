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
    @Lob
    private String body;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;
    @ManyToOne
    private Module module;
    private Double averageContentRating;
    @Builder.Default
    private Boolean isDeleted = false;
}
