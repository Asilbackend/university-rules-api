package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification  n where n.user.id=:userId and n.is_read")
    Page<Notification> findAllByUserIdAndIs_read(Long userId, Boolean isRead, Pageable pageable);
    Page<Notification> findAllByIsDeletedFalse(Pageable pageable);
}
