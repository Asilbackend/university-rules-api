package uz.tuit.unirules.entity.content_student;

import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.user.User;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"attachment_id", "student_id"})
        }
)
public class AttachmentStudent extends BaseEntity {
    //uodatedAt orqalia oxirgi marta korilgani kontrol qilinadi
    @ManyToOne
    private Attachment attachment; // content ichidagi attachmentlardan biri bolishi kerak!!
    @ManyToOne
    private User student;
    @Min(0)
    @Max(5)
    @Builder.Default
    private Integer rating = null;
    private Boolean isRead;
    /**
     * Majburiy contentlar uchun qaysi joygacha o‘qilgani (masalan, % progress, yoki position)
     */
    private Double progress = 0.0;
}
