package uz.tuit.unirules.services.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.projections.CommentProjection;
import uz.tuit.unirules.entity.comment.Comment;
import uz.tuit.unirules.entity.comment.CommentRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthUserService authUserService;
    private final AttachmentService attachmentService;

    public CommentService(CommentRepository commentRepository, AuthUserService authUserService, AttachmentService attachmentService) {
        this.commentRepository = commentRepository;
        this.authUserService = authUserService;
        this.attachmentService = attachmentService;
    }

    public void setComment(Long attachmentId, String comment) {
        if (comment == null || comment.isEmpty()) {
            throw new RuntimeException("don't send empty comment");
        }
        commentRepository.save(
                Comment.builder()
                        .user(authUserService.getAuthUser())
                        .attachment(attachmentService.findById(attachmentId))
                        .comment(comment)
                        .build());
    }

    public List<CommentProjection> getComments(Long lastCommentId, Integer size, Long attachmentId) {
        boolean isInitial = lastCommentId == null;
        return commentRepository.getComments(lastCommentId, size, authUserService.getAuthUserId(), attachmentId, isInitial);
    }

    @Transactional
    public void deleteByAuthUser(Long commentId, Long authUserId) {
        commentRepository.softDeleteByIdAndUserId(commentId, authUserId);
    }

    public void updateByAuthUser(Long commentId, Long authUserId, String updatedComment) {
        try {
            Optional<Comment> comment = commentRepository.findByCommentIdAndUserIdAndDeletedFalse(commentId, authUserId);
            comment.ifPresent(c -> {
                c.setComment(updatedComment);
                commentRepository.save(c);
            });
        } catch (Exception e) {
            throw new RuntimeException("comment error");
        }
    }
}
