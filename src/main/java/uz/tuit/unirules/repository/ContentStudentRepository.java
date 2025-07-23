package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.content_student.ContentStudent;

import java.util.List;
import java.util.Optional;

public interface ContentStudentRepository extends JpaRepository<ContentStudent, Long> {
    Optional<ContentStudent> findByContentId(Long contentId);

    /*@Query(value = """
                           select cs
                           from content_student cs
                                    join content c on cs.content_id = c.id
                                    join users u on cs.user_id = u.id
                           where c.id = :contentId
                             and u.id = :authUserId
                             and c.is_deleted = false
                             and cs.is_deleted = false;
               """, nativeQuery = true)*/
    @Query("select cs from ContentStudent cs where cs.content.id=:contentId and cs.user.id=:authUserId and cs.isDeleted=false and cs.content.isDeleted=false")
    Optional<ContentStudent> findByContentIdAndUserId(Long contentId, Long authUserId);
  /*  @Query("select cs from ContentStudent  cs where cs.content.attachments in (select a from Attachment a where a.id=:attachmentId) and cs.user.id=:userId limit 1")
    Optional<ContentStudent> findByUserIdAndAttachmentId(Long userId, Long attachmentId);

    @Query("select c from Content c where c.attachments in (select a from Attachment a where a.id=:attachmentId) limit 1")
    Optional<Content> findContentByAttachmentId(Long attachmentId);*/
}
