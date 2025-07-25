package uz.tuit.unirules.controller.student;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.projections.FuzzySearchProjection;
import uz.tuit.unirules.projections.TopVideoProjection;
import uz.tuit.unirules.repository.AttachmentStudentRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.content.ContentService;

import java.util.List;

@RestController
@RequestMapping("/api/student/dashboard")
public class HomePageController {
    private final AuthUserService authUserService;
    private final AttachmentStudentRepository attachmentStudentRepository;
    private final AttachmentStudentService attachmentStudentService;
    private final ContentService contentService;


    public HomePageController(AuthUserService authUserService,
                              AttachmentStudentRepository attachmentStudentRepository, AttachmentStudentService attachmentStudentService, ContentService contentService) {
        this.authUserService = authUserService;
        this.attachmentStudentRepository = attachmentStudentRepository;
        this.attachmentStudentService = attachmentStudentService;
        this.contentService = contentService;
    }

    @GetMapping("/topVideos")
    public Page<TopVideoProjection> getTopVideos(@ParameterObject Pageable pageable) {
        return attachmentStudentService.getTopVideos(pageable);
    }
    @GetMapping("/fuzzy")
    public List<FuzzySearchProjection> fuzzySearchContentModuleContentElementByTitle(@RequestParam String title){
        return contentService.fuzzySearchContentModuleContentElementByTitle(title);
    }
}
