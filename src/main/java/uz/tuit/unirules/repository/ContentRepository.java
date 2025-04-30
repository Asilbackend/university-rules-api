package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.projections.ContentProjection;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {
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
            WHERE c.id = :id
            """, nativeQuery = true)
    Optional<ContentProjection> findContentById(Long id);

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
            WHERE c.is_deleted=:isDelete
            """, nativeQuery = true)
    List<ContentProjection> findAllContents(Boolean isDelete);

}
