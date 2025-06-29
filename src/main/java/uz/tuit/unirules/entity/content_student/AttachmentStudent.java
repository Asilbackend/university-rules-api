package uz.tuit.unirules.entity.content_student;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class AttachmentStudent extends BaseEntity {
    @ManyToOne
    private Attachment attachment; // content ichidagi attachmentlardan biri bolishi kerak!!
    @ManyToOne
    private ContentStudent contentStudent;
    @Min(0)
    @Max(5)
    private Integer rating;
    private String comment;
    /**
     * Majburiy contentlar uchun qaysi joygacha o‘qilgani (masalan, % progress, yoki position)
     */
    private Double progress = 0.0;
    @PrePersist
    @PreUpdate
    private void validateRatingComment() {
        if (rating != null && rating < 3 && (comment == null || comment.trim().isEmpty())) {
            throw new IllegalArgumentException("3 dan past baho uchun comment majburiy");
        }
    }
}
