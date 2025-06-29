package uz.tuit.unirules.controller.student;


import org.springframework.core.io.support.ResourceRegion;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.attachment_student.VideoService;

import java.io.IOException;


@RestController
@RequestMapping("/api/student/content-video")
public class ContentVideoController {
    private final AttachmentStudentService attachmentStudentService;
    private final AuthUserService authUserService;
    private final VideoService videoService;

    public ContentVideoController(AttachmentStudentService attachmentStudentService, AuthUserService authUserService, VideoService videoService) {
        this.attachmentStudentService = attachmentStudentService;
        this.authUserService = authUserService;
        this.videoService = videoService;
    }

    @PutMapping
    void updateVideoPercent(@RequestParam Long attachmentId, @RequestParam Double percent) {
        attachmentStudentService.updateVideoPercent(authUserService.getAuthUserId(), attachmentId, percent);
    }

    @GetMapping("/{attachmentId}/stream")
    public ResponseEntity<ResourceRegion> streamVideo(
            @PathVariable Long attachmentId,
            @RequestHeader HttpHeaders headers) throws IOException {
        return videoService.prepareVideoRegion(attachmentId, headers);
    }
}
