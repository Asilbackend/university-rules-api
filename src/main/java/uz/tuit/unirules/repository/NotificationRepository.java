package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
