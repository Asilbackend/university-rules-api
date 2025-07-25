package uz.tuit.unirules.entity.content;

import jakarta.persistence.*;

import lombok.*;
import org.springframework.lang.Nullable;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"content_id", "attachment_id"})
        }
)
public class ContentElement extends BaseEntity {
    @Column(unique = true)
    private String title;// faqat text uchun title
    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private Attachment attachment;
    @Lob
    @Column(columnDefinition = "TEXT")
    @Basic(fetch = FetchType.EAGER)
    private String text; //style ga ega text
    private Integer orderElement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @PrePersist
    @PreUpdate
    public void validate() {
        if ((this.text == null || this.text.trim().isEmpty()) && this.attachment == null) {
            throw new IllegalStateException("Kamida text yoki attachment kerak");
        }

    }
}

