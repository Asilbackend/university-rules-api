package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;
import uz.tuit.unirules.projections.TemporaryRequiredContentProjection;
import uz.tuit.unirules.projections.TopVideoProjection;

import java.util.List;
import java.util.Optional;

public interface AttachmentStudentRepository extends JpaRepository<AttachmentStudent, Long> {


    /*@Query("select ast from AttachmentStudent ast where ast.contentStudent.isDeleted=false and ast.contentStudent.content.isDeleted=false order by ast.rating desc limit 10")
    List<AttachmentStudent> findAllByRatingDesc();*/

    /*@Query("select ast from AttachmentStudent ast where ast.contentStudent.content.id=:id and ast.contentStudent.isDeleted=false")
    List<AttachmentStudent> findByContentId(Long id);
*/
    @Query("select ast from AttachmentStudent ast where ast.student.id=:userId and ast.attachment.id=:attachmentId")
    Optional<AttachmentStudent> findByStudentIdAndAttachmentId(Long userId, Long attachmentId);

   /* @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ast FROM AttachmentStudent ast WHERE ast.attachment.id = :attachmentId AND ast.contentStudent.user.id=:studentId and ast.contentStudent.isDeleted=false ORDER BY ast.createdAt desc")
    List<AttachmentStudent> findLatestByStudentIdAndAttachmentIdForUpdate(Long studentId, Long attachmentId);*/


    /*@Query(value = """
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
                """, nativeQuery = true)
    RecommendModuleNewProjection findRecommendedLastModule(@Param(value = "userId") Long userId);*/

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
            """, nativeQuery = true)
    TemporaryRequiredContentProjection findLastRequiredContentPro(@Param(value = "id") Long userId);

    @Query(value = """
            select ats.attachment_id              as attachmentId,
                   ats.progress                   as progress,
                   coalesce(a.title, ce.ce_title) as title,
                   ce.description                 as description,
                   a.thumbnail_image_url          as thumbnailImageUrl
            from attachment_student ats
                     join attachment a on ats.attachment_id = a.id
                     left join lateral (
                select ce.title as ce_title, ce.description as description
                from content_element ce
                where ce.attachment_id = a.id
                limit 1
                ) ce on true
            where ats.student_id = :id
              and ats.updated_at is not null
              and a.attach_type = :attachType
            order by ats.updated_at desc;
            """,
            nativeQuery = true)
    Page<AttachmentProjection> findLastUpdatedAttachments(@Param("id") Long userId, @Param("attachType") String attachType, Pageable pageable);

    @Query(value = """
            select coalesce(a.title, ce.title) as title,
                   ats.attachment_id           as attachmentId,
                   a.thumbnail_image_url       as thumbnailImageUrl,
                   ce.is_required              as required,
                   ce.description              as description
            from attachment_student ats
                     join attachment a on ats.attachment_id = a.id
                     cross join lateral (
                select ce.title, c.id, c.is_required, ce.description
                from content_element ce
                         join content c on ce.content_id = c.id
                where ce.attachment_id = ats.id
                limit 1
                ) ce
            where a.attach_type = :at_type
              and a.is_deleted = false
            order by ats.rating desc nulls last;
            """, nativeQuery = true)
    Page<TopVideoProjection> findAllTopVideos(String at_type, Pageable pageable);

    @Query(value = """
            select avg(ast.rating)
            from attachment_student ast
                     join attachment a on ast.attachment_id = a.id
            where a.id = :attachmentId
                        """, nativeQuery = true)
    Double calculateAverageRateByAttachmentId(Long attachmentId);
}
