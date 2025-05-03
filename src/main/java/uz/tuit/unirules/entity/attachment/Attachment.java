package uz.tuit.unirules.entity.attachment;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.content.Content;

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
    @JoinColumn(name = "content_id")
    private Content content;
    @Builder.Default
    private Boolean isDeleted = false;

    public enum AttachType {
        VIDEO, PICTURE, AUDIO, DOCUMENT, ANY
    }
  /*  DOCUMENT — bu yerda PDF, DOCX, PPTX, XLSX, TXT va
  hokazolarni birlashtirgan umumiy "o‘qiladigan fayl" toifasi sifatida ishlatiladi.
*/

}
