package uz.tuit.unirules.entity.content_student;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.user.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "content_student")
public class ContentStudent extends BaseEntity {
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Content content;
    @Min(0)
    @Max(5)
    private Integer rating;
    private String comment;
    /**
     * Majburiy contentlar uchun qaysi joygacha o‘qilgani (masalan, % progress, yoki position)
     */
    private Double progress = 0.0;
    /**
     * User contentni to‘liq o‘qiganmi yoki yo‘q
     */
    private boolean isRead = false;
    /**
     * Contentni to‘liq o‘qib chiqqan vaqt
     */
    private LocalDateTime readAt;
    /**
     * User contentni o‘qishni boshlagan vaqti
     */
    private LocalDateTime startedAt;
    /**
     * ContentService majburiymi user uchun yoki yo‘q
     */
    private boolean isRequired = false;

    @PrePersist
    @PreUpdate
    private void validateRatingComment() {
        if (rating != null && rating < 3 && (comment == null || comment.trim().isEmpty())) {
            throw new IllegalArgumentException("3 dan past baho uchun comment majburiy");
        }
    }

    public enum UserContentStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private UserContentStatus status = UserContentStatus.NOT_STARTED;
}
