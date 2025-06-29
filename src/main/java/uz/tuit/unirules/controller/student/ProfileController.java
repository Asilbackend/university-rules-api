package uz.tuit.unirules.controller.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.projections.CertificateProjection;
import uz.tuit.unirules.projections.RecommendModuleNewProjection;
import uz.tuit.unirules.projections.TemporaryRequiredContentProjection;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.certificate.CertificateService;

import java.util.List;

@RestController
@RequestMapping("/api/student/profile")
public class ProfileController {
    private final CertificateService certificateService;
    private final AttachmentStudentService attachmentStudentService;

    public ProfileController(CertificateService certificateService, AttachmentStudentService attachmentStudentService) {
        this.certificateService = certificateService;
        this.attachmentStudentService = attachmentStudentService;
    }
    @GetMapping("/last-module")
    public RecommendModuleNewProjection getRecommendedLastModule(){
        return this.attachmentStudentService.getRecommendedModuleLast();
    }
    @GetMapping("/get-required/content")
    public TemporaryRequiredContentProjection getRequiredContent(){
        return this.attachmentStudentService.getRequiredContentProjection();
    }
    @GetMapping("/certificates")
    public List<CertificateProjection> getLastThreeCertificates(){
        return this.certificateService.getLastThreeCertificates();
    }
}
