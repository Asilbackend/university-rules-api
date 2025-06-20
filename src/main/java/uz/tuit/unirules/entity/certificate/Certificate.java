package uz.tuit.unirules.entity.certificate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.user_test.UserTest;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Certificate extends BaseEntity {
    @ManyToOne
    @JoinColumn
    private UserTest userTest;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Attachment attachment;
    private LocalDate issuedAt; //berilgan sana
    @Builder.Default
    private Boolean isDeleted = false;
}
