package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.news.News;
import uz.tuit.unirules.projections.AttachmentNewsProjection;
import uz.tuit.unirules.projections.AttachmentUrlProjection;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Long> {

    List<News> findAllByIsDeletedFalse();
    Page<News> findAllByIsDeletedFalse(Pageable pageable);
    @Query(value = """
            SELECT 
            n.id AS newsId,
            a.id AS attachmentId,
            a.thumbnail_image_url as thumbNailUrl,
            n.name AS name,
            n.description AS description
            FROM news n 
            LEFT JOIN attachment a ON n.attachment_id = a.id
            WHERE n.is_deleted = false
            """,nativeQuery = true)
    Page<AttachmentNewsProjection> findAttachmentNews(Pageable pageable);
    @Query(value = """
            SELECT 
            a.url as url
            FROM news n 
            left join attachment a on n.attachment_id = a.id
            WHERE n.id =:id
            limit 1
            """,nativeQuery = true)
    AttachmentUrlProjection findAttachmentUrl(@Param(value = "id") Long newsId);
}
