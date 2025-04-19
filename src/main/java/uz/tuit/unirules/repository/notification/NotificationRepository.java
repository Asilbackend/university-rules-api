package uz.tuit.unirules.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.notification.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
