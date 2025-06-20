package uz.tuit.unirules.controller.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.dto.respond_dto.TopVideo;
import uz.tuit.unirules.repository.AttachmentStudentRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;

import java.util.ArrayList;
import java.util.List;

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
    public List<TopVideo> getTopVideos() {
        return attachmentStudentService.getTopVideos();
    }
}
