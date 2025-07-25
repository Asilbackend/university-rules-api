package uz.tuit.unirules.entity.news;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.user.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"news_id", "student_id"})
        }
)
public class NewsStudent extends BaseEntity {
    private Boolean isSeen;
    private Boolean isLike;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private News news;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User student;
}
