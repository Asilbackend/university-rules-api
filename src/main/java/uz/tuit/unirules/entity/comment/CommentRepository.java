package uz.tuit.unirules.entity.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.projections.CommentProjection;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
            select c.comment,
                   u.firstname,
                   u.lastname,
                   u.id as userId
            from comment c
                     join users u on u.id = c.user_id
            where c.attachment_id = :attachmentId
              and (:isInitial or c.id >= :lastCommentId)
            order by c.created_at desc
            limit :sized
            """, nativeQuery = true)
    List<CommentProjection> getComments(Long lastCommentId, Integer sized, Long userId, Long attachmentId, boolean isInitial);
}