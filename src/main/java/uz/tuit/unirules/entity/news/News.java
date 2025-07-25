package uz.tuit.unirules.entity.news;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class News extends BaseEntity {
    private String name;
    private String description;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted=false;
    @ManyToOne(fetch = FetchType.EAGER)
    private Attachment attachment;
}
