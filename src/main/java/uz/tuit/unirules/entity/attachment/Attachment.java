package uz.tuit.unirules.entity.attachment;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.modul.Module;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Attachment extends BaseEntity {
    @Builder.Default
    private String fileId = UUID.randomUUID().toString();
    private String url;
    private String fileName;
    @Enumerated(EnumType.STRING)
    private AttachType attachType;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    public enum AttachType {
        VIDEO, PICTURE, PDF_OR_ANY, AUDIO
    }


}
