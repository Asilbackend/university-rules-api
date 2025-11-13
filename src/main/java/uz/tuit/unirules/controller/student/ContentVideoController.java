package uz.tuit.unirules.controller.student;


import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.projections.CommentProjection;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.attachment_student.VideoService;
import uz.tuit.unirules.services.comment.CommentService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/student/content-video")
public class ContentVideoController {
    private final AttachmentStudentService attachmentStudentService;
    private final AuthUserService authUserService;
    private final VideoService videoService;
    private final CommentService commentService;

    public ContentVideoController(AttachmentStudentService attachmentStudentService, AuthUserService authUserService, VideoService videoService, CommentService commentService) {
        this.attachmentStudentService = attachmentStudentService;
        this.authUserService = authUserService;
        this.videoService = videoService;
        this.commentService = commentService;
    }

    @PutMapping
    void updateVideoPercent(@RequestParam Long attachmentId, @RequestParam Double percent, @RequestParam Long contentId) {
        attachmentStudentService.updateVideoPercent(authUserService.getAuthUserId(), attachmentId, percent, contentId);
    }

    @GetMapping("/{attachmentId}/stream")
    public ResponseEntity<ResourceRegion> streamVideo(
            @PathVariable Long attachmentId,
            @RequestHeader HttpHeaders headers) throws IOException {
        return videoService.prepareVideoRegion(attachmentId, headers);
    }

    @PostMapping("/rate-content")
    public HttpEntity<?> rateVideo(@RequestParam Integer videoRate,
                                   @RequestParam Long attachmentId
    ) {
        return attachmentStudentService.ratingVideo(videoRate, attachmentId);
    }

    @GetMapping("/rate-content")
    public HttpEntity<?> getRatingVideoOfUser(@RequestParam Long attachmentId) {
        return attachmentStudentService.getUserRatingVideo(attachmentId);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> SaveComment(
            @RequestParam Long attachmentId,
            @RequestParam String comment) {
        commentService.setComment(attachmentId, comment);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comment")
    public ResponseEntity<?> getComments(
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam Integer size,
            @RequestParam Long attachmentId

    ) {
        List<CommentProjection> commentProjections = commentService.getComments(lastCommentId, size, attachmentId);
        return ResponseEntity.ok(commentProjections);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteCommentByAuthUser(
            @RequestParam Long commentId
    ) {
        commentService.deleteByAuthUser(commentId, authUserService.getAuthUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(
            @RequestParam Long commentId,
            @RequestParam String updatedComment
    ) {
        commentService.updateByAuthUser(commentId, authUserService.getAuthUserId(), updatedComment);
        return ResponseEntity.noContent().build();
    }
}
