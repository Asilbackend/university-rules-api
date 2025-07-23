package uz.tuit.unirules.services.comment;

import org.springframework.stereotype.Service;
import uz.tuit.unirules.projections.CommentProjection;
import uz.tuit.unirules.entity.comment.Comment;
import uz.tuit.unirules.entity.comment.CommentRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.util.List;

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
        return commentRepository.getComments(lastCommentId, size, authUserService.getAuthUserId(), attachmentId,isInitial);
    }
}
