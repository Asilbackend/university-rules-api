package uz.tuit.unirules.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.projections.ContentProjection;
import uz.tuit.unirules.projections.ContentRespProjection;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {
    /*@Query(value = """
            SELECT
                c.id AS id,
                c.title AS title,
                c.body AS body,
                ARRAY(
                    SELECT a.id
                    FROM attachment a
                    WHERE a.content_id = c.id
                ) AS attachmentIds,
                c.module_id AS moduleId,
                c.average_content_rating AS averageContentRating
            FROM content c
            WHERE c.id = :id
            """, nativeQuery = true)
    Optional<ContentProjection> findContentById(Long id);*/


    @Query(value = """
            SELECT
                c.id AS id,
                c.title AS title,
                c.body AS body,
                ARRAY(
                    SELECT a.id
                    FROM attachment a
                    WHERE a.content_id = c.id
                ) AS attachmentIds,
                c.module_id AS moduleId,
                c.average_content_rating AS averageContentRating
            FROM content c
            WHERE c.is_deleted=:false and c.module_id=:moduleId
            """, nativeQuery = true)
    Page<ContentProjection> findAllByModuleId(Long moduleId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Content c where c.id = :id and c.isDeleted=false")
    Optional<Content> findByIdForUpdate(@Param("id") Long id);

    @Query(value = """
            SELECT c.id    AS contentId,
                   c.title as contentTitle,
                   array_agg(
                           json_strip_nulls(
                                   json_build_object(
                                           'contentType', CASE
                                                              WHEN a.id IS NULL THEN 'text'
                                                              ELSE a.attach_type
                                       END,
                                           'videoDuration', a.video_duration,
                                           'isRead', coalesce(ats.is_read, false),
                                           'title', ce.title,
                                           'order_element', ce.order_element
                                   )
                           )
                   )       AS attachmentDetails
            FROM content_element ce
                     LEFT JOIN content c ON ce.content_id = c.id
                     LEFT JOIN attachment a ON ce.attachment_id = a.id
                     LEFT JOIN attachment_student ats on a.id = ats.attachment_id
                and ats.student_id = :userId
                     LEFT JOIN content_student cs on cs.content_id = c.id
                AND cs.user_id = :userId
                AND cs.is_deleted = false
            WHERE c.module_id = :moduleId
              and c.is_deleted = false
            group by c.id, ce.title, ce.order_element
            order by ce.order_element;
            """, nativeQuery = true)
    List<ContentRespProjection> findAllByModuleIdAndUserId(Long moduleId, Long userId);

    List<Content> findAllByIsDeleted(Boolean isDeleted);
}
