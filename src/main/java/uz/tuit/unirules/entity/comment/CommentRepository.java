package uz.tuit.unirules.entity.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.projections.CommentProjection;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
            select c.id             as commentId,
                   c.comment,
                   u.firstname,
                   u.lastname,
                   u.id             as userId,
                   (u.id = :userId) AS own
            from comment c
                     join users u on u.id = c.user_id
            where c.attachment_id = :attachmentId
              and (:isInitial or c.id >= :lastCommentId)
              and c.is_deleted = false
            order by c.created_at desc
            limit :sized
            """, nativeQuery = true)
    List<CommentProjection> getComments(Long lastCommentId, Integer sized, Long userId, Long attachmentId, boolean isInitial);

    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true WHERE c.id = :commentId AND c.user.id = :userId")
    void softDeleteByIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query("select c from Comment c where c.id=:commentId and c.user.id=:userId and c.isDeleted=false")
    Optional<Comment> findByCommentIdAndUserIdAndDeletedFalse(Long commentId, Long userId);
}