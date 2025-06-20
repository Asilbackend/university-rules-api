package uz.tuit.unirules.controller.student;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.content.ContentService;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.List;

@RestController
@RequestMapping("/api/student/module-content")
public class ModuleContentController {
    private final ModuleService moduleService;
    private final ContentService contentService;
    private final AttachmentStudentService attachmentStudentService;

    public ModuleContentController(ModuleService moduleService, ContentService contentService, AttachmentStudentService attachmentStudentService) {
        this.moduleService = moduleService;
        this.contentService = contentService;
        this.attachmentStudentService = attachmentStudentService;
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
    public ApiResponse<List<ContentRespDto>> getAllByModuleId(@RequestParam Long moduleId,
                                                              @ParameterObject Pageable pageable) {
        return contentService.getAllByModuleId(moduleId, pageable);
    }

    @PostMapping("/rate-content")
    public HttpEntity<?> rateVideo(@RequestParam Integer videoRate,
                                   @RequestParam Long attachmentId
    ) {
        return attachmentStudentService.ratingVideo(videoRate, attachmentId);
    }
}
