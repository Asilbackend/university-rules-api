package uz.tuit.unirules.entity.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContentElementStudentRepository extends JpaRepository<ContentElementStudent, Long> {
    @Query("select ces from ContentElementStudent  ces where ces.contentElement.id =:contentElementId and ces.student.id =:authUserId and  ces.contentElement.content.isDeleted=false")
    Optional<ContentElementStudent> findByContentElementIdAndStudentId(Long contentElementId, Long authUserId);

    @Query("""
            select count(ces) from ContentElementStudent ces where
              ces.student.id=:userId and ces.contentElement.content.id=:contentId
              and ces.contentElement.content.isDeleted=false
              and ces.isRead=true
                    """)
    Integer countByIsReadTrue(Long contentId, Long userId);
}