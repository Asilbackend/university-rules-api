package uz.tuit.unirules.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;

import java.util.List;

public interface AttachmentStudentRepository extends JpaRepository<AttachmentStudent, Long> {


    @Query("select ast from AttachmentStudent ast where ast.contentStudent.isDeleted=false and ast.contentStudent.content.isDeleted=false order by ast.rating desc limit 10")
    List<AttachmentStudent> findAllByRatingDesc();

    @Query("select ast from AttachmentStudent ast where ast.contentStudent.content.id=:id and ast.contentStudent.isDeleted=false")
    List<AttachmentStudent> findByContentId(Long id);
    @Query("select ast from AttachmentStudent ast where ast.contentStudent.user.id=:userId and ast.attachment.id=:attachmentId and ast.contentStudent.isDeleted=false")
    List<AttachmentStudent> findByStudentIdAndAttachmentId(Long userId, Long attachmentId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ast FROM AttachmentStudent ast WHERE ast.attachment.id = :attachmentId AND ast.contentStudent.user.id=:studentId and ast.contentStudent.isDeleted=false ORDER BY ast.createdAt desc")
    List<AttachmentStudent> findLatestByStudentIdAndAttachmentIdForUpdate(Long studentId, Long attachmentId);

}
