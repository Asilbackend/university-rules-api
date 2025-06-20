package uz.tuit.unirules.entity.support_request;

import jakarta.persistence.*;
import lombok.*;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class SupportRequest extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private User supportUser;
    private String subject;
    @Lob
    private String message;
    @Lob
    private String responseMessage;
    @Builder.Default
    private Boolean isDeleted = false;
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.OPEN;

    public enum Status {
        OPEN, //so'rov yuborildi
        PENDING, // so'rov qabul qilindi ko'rib chiqilyapti
        IN_PROGRESS,// so'rov ko'rib chiqilyapti
        CLOSED,// javob berildi
        CANCELLED // So‘rov foydalanuvchi yoki admin tomonidan bekor qilingan.
    }
}
