package uz.tuit.unirules.controller.student;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.projections.TopVideoProjection;
import uz.tuit.unirules.repository.AttachmentStudentRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;

@RestController
@RequestMapping("/api/student/dashboard")
public class HomePageController {
    private final AuthUserService authUserService;
    private final AttachmentStudentRepository attachmentStudentRepository;
    private final AttachmentStudentService attachmentStudentService;

    public HomePageController(AuthUserService authUserService,
                              AttachmentStudentRepository attachmentStudentRepository, AttachmentStudentService attachmentStudentService) {
        this.authUserService = authUserService;
        this.attachmentStudentRepository = attachmentStudentRepository;
        this.attachmentStudentService = attachmentStudentService;

    }

    @GetMapping("/topVideos")
    public Page<TopVideoProjection> getTopVideos(@ParameterObject Pageable pageable) {
        return attachmentStudentService.getTopVideos(pageable);
    }
}
