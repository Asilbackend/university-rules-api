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
            WHERE (:isInitial or n.created_at < :lastCreatedAt)
              and n.is_deleted = false
            ORDER BY n.created_at DESC
            limit :limited
            """, nativeQuery = true)
    List<AttachmentNewsProjection> findNextStoriesNative(@Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                                         @Param("limited") int limited,
                                                         @Param("isInitial") boolean isInitial,
                                                         @Param("userId") Long userId);

    @Query(value = """
            select a.id                                                               as attachmentId,
                   a.thumbnail_image_url                                              as thumbNailUrl,
                   (select count(ns1) from news_student ns1 where ns1.is_like = true) as likeCount,
                   ns.is_like                                                         as isLiked,
                   n.name                                                             as title,
                   n.description                                                      as description,
                   a.video_duration                                                   as videoDuration
            from news n
                     join attachment a on a.id = n.attachment_id
                     left join news_student ns on ns.news_id = n.id
                and ns.student_id = :userId
            where n.is_deleted = false
              and a.is_deleted = false
              and n.id = :newsId
            """, nativeQuery = true)
    Optional<NewsVideoProjection> findNewsVideoInfo(Long newsId, Long userId);

}
