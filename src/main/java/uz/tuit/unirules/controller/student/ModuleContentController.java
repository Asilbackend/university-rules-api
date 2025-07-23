package uz.tuit.unirules.controller.student;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.entity.content.ContentElementRepository;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.projections.CommentProjection;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.comment.CommentService;
import uz.tuit.unirules.services.content.ContentService;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.List;

@RestController
@RequestMapping("/api/student/module-content")
public class ModuleContentController {
    private final ModuleService moduleService;
    private final ContentService contentService;
    private final AttachmentStudentService attachmentStudentService;
    private final ContentElementRepository contentElementRepository;
    private final CommentService commentService;

    public ModuleContentController(ModuleService moduleService, ContentService contentService, AttachmentStudentService attachmentStudentService,
                                   ContentElementRepository contentElementRepository, CommentService commentService) {
        this.moduleService = moduleService;
        this.contentService = contentService;
        this.attachmentStudentService = attachmentStudentService;
        this.contentElementRepository = contentElementRepository;
        this.commentService = commentService;
    }

    @GetMapping("/module/{id}")
    public ApiResponse<Module> get(@PathVariable Long id) {
        return moduleService.getModuleVisible(id);
    }

    @GetMapping("/module")
    public Page<Module> getAllPagination(
            @ParameterObject Pageable pageable
    ) {
        return moduleService.getAllPaginationVisible(pageable);
    }

    @GetMapping("/findContents-byModuleId")
    public List<?> getAllByModuleId(@RequestParam Long moduleId) {
        return contentService.getAllByModuleId(moduleId);
    }

    @GetMapping("/findContentTextByTitle")
    public String findContentTextByTitle(@RequestParam String title, Long contentId) {
        Long contentElementId = contentElementRepository.findIdByContentIdAndTitle(contentId, title);
        contentService.readContentElementFromContent(contentElementId);
        return contentService.getTextByTitle(title);
    }

    @PostMapping("/readFileFromContent")
    public void readFileFromContent(@RequestParam Long attachmentId, @RequestParam Long contentId) {
        Long contentElementId = contentElementRepository.findContentElementIdByContentIdAndAttachmentId(contentId, attachmentId);
        contentService.readContentElementFromContent(contentElementId);
    }

    @PostMapping("/rate-content")
    public HttpEntity<?> rateVideo(@RequestParam Integer videoRate,
                                   @RequestParam Long attachmentId
    ) {
        return attachmentStudentService.ratingVideo(videoRate, attachmentId);
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

    @PostMapping("/startContent")
    public ResponseEntity<?> StartContent(
            @RequestParam Long contentId) {
        contentService.startContent(contentId);
        return ResponseEntity.noContent().build();
    }

}
