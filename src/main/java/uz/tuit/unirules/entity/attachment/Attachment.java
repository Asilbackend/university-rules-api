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
    /* @Builder.Default
     private String fileId = UUID.randomUUID().toString();*/
    private String url;
    @Builder.Default
    private String thumbnailImageUrl = null; //video yoki boshqa katta fayllar uchun rasm
    private String fileName;
    @Enumerated(EnumType.STRING)
    private AttachType attachType;
    @Builder.Default
    private Boolean isDeleted = false;
    private String title; //sarlavha
    private String videoDuration;

    public enum AttachType {
        VIDEO, PICTURE, AUDIO, DOCUMENT, ANY
    }
  /*  DOCUMENT — bu yerda PDF, DOCX, PPTX, XLSX, TXT va
  hokazolarni birlashtirgan umumiy "o‘qiladigan fayl" toifasi sifatida ishlatiladi.
*/

}
