package uz.tuit.unirules.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;
import uz.tuit.unirules.projections.RecommendModuleNewProjection;
import uz.tuit.unirules.projections.TemporaryRequiredContentProjection;

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


    @Query(value = """
            SELECT c.title               as contentName,
                       rm.reason             as reason,
                       a.id                  as attachmentId,
                       a.thumbnail_image_url as thumbNailUrl
                FROM attachment a
                         left join recommended_module rm on a.user_id = rm.user_id
                         left join users us on rm.user_id = us.id
                         left join module m on m.id = rm.module_id
                         left join content c on c.module_id = m.id
                WHERE us.id =:userId
                order by a.created_at desc limit 1;
                """,nativeQuery = true)
    RecommendModuleNewProjection findRecommendedLastModule(@Param(value = "userId") Long userId);

    @Query(value = """
            select
                ast.id as attachmentId,
                a.thumbnail_image_url as thumbNailUrl
            from attachment_student ast
            left join attachment a on ast.attachment_id = a.id
            left join content_student cs on ast.content_student_id=cs.id
            left join users us on ast.user_id = us.id
            where us.id =:id
            order by cs.started_at desc  limit 1;
            """,nativeQuery = true)
    TemporaryRequiredContentProjection findLastRequiredContentPro(@Param(value = "id") Long userId);
}
