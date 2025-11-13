package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.projections.NewsVideoProjection;
import uz.tuit.unirules.entity.news.News;
import uz.tuit.unirules.projections.AttachmentNewsProjection;
import uz.tuit.unirules.projections.AttachmentUrlProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByIsDeletedFalse();

    Page<News> findAllByIsDeletedFalse(Pageable pageable);

    @Query(value = """
            SELECT 
            n.id AS newsId,
            a.id AS attachmentId,
            a.thumbnail_image_url as thumbNailUrl
            FROM news n 
            LEFT JOIN attachment a ON n.attachment_id = a.id
            WHERE n.is_deleted = false
            """, nativeQuery = true)
    Page<AttachmentNewsProjection> findAttachmentNews(Pageable pageable);

    @Query(value = """
            SELECT
            a.url as url
            FROM news n
            left join attachment a on n.attachment_id = a.id
            WHERE n.id =:id
            limit 1
            """, nativeQuery = true)
    AttachmentUrlProjection findAttachmentUrl(@Param(value = "id") Long newsId);


    @Query(value = """
            SELECT n.id                        AS newsId,
                   a.id                        AS attachmentId,
                   a.thumbnail_image_url       as thumbNailUrl,
                   coalesce(ns.is_seen, false) as isSeen
            FROM news n
                     JOIN attachment a on a.id = n.attachment_id
                     LEFT JOIN news_student ns on ns.student_id = :userId
                and ns.news_id = n.id
            WHERE (:isInitial or n.id < :lastId)
              and n.is_deleted = false
            ORDER BY n.id DESC
            limit :limited
            """, nativeQuery = true)
    List<AttachmentNewsProjection> findNextStoriesNative(@Param("lastId") Long lastId,
                                                         @Param("limited") int limited,
                                                         @Param("isInitial") boolean isInitial,
                                                         @Param("userId") Long userId);

    @Query(value = """
            SELECT a.id                           AS attachmentId,
                   a.thumbnail_image_url          AS thumbNailUrl,
                   a.url                          as videoUrl,
                   COALESCE(counts.like_count, 0) AS likeCount,
                   coalesce(ns.is_like, false)    AS isLiked,
                   n.name                         AS title,
                   n.description                  AS description,
                   a.video_duration               AS videoDuration,
                   COALESCE(counts.seen_count, 0) AS seenCount
            FROM news n
                     JOIN attachment a ON a.id = n.attachment_id
                     LEFT JOIN news_student ns ON ns.news_id = n.id AND ns.student_id = :userId
                     LEFT JOIN (SELECT ns1.news_id,
                                       COUNT(*) FILTER (WHERE ns1.is_like = true) AS like_count,
                                       COUNT(*) FILTER (WHERE ns1.is_seen = true) AS seen_count
                                FROM news_student ns1
                                GROUP BY ns1.news_id) counts ON counts.news_id = n.id
            WHERE n.is_deleted = false
              AND a.is_deleted = false
              AND n.id = :newsId
            order by n.id desc;
            """, nativeQuery = true)
    Optional<NewsVideoProjection> findNewsVideoInfo(Long newsId, Long userId);

}
