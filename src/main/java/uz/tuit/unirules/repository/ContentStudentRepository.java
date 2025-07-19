package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.content_student.ContentStudent;

import java.util.List;
import java.util.Optional;

public interface ContentStudentRepository extends JpaRepository<ContentStudent, Long> {
    Optional<ContentStudent> findByContentId(Long contentId);

    List<ContentStudent> findByContentIdAndUserId(Long contentId, Long authUserId);
  /*  @Query("select cs from ContentStudent  cs where cs.content.attachments in (select a from Attachment a where a.id=:attachmentId) and cs.user.id=:userId limit 1")
    Optional<ContentStudent> findByUserIdAndAttachmentId(Long userId, Long attachmentId);

    @Query("select c from Content c where c.attachments in (select a from Attachment a where a.id=:attachmentId) limit 1")
    Optional<Content> findContentByAttachmentId(Long attachmentId);*/
}
