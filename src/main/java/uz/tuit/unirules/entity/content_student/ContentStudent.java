package uz.tuit.unirules.entity.content_student;

import jakarta.persistence.*;

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
public class ContentStudent extends BaseEntity {
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Content content;
    @Builder.Default
    private Boolean isDeleted = false;

   /* *//**
     * User contentni to‘liq o‘qiganmi yoki yo‘q
     *//*
    @Builder.Default
    private boolean isRead = false;*/
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
    @Builder.Default
    private boolean isRequired = false;


    public enum UserContentStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserContentStatus status = UserContentStatus.NOT_STARTED;
}
