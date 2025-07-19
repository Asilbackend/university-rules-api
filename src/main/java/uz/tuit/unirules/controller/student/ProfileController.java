package uz.tuit.unirules.controller.student;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.projections.CertificateProjection;
import uz.tuit.unirules.projections.ModuleUserProjection;
import uz.tuit.unirules.projections.RecommendModuleNewProjection;
import uz.tuit.unirules.projections.TemporaryRequiredContentProjection;
import uz.tuit.unirules.repository.AttachmentProjection;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.certificate.CertificateService;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/profile")
public class ProfileController {
    private final CertificateService certificateService;
    private final AttachmentStudentService attachmentStudentService;
    private final ModuleService moduleService;

    public ProfileController(CertificateService certificateService, AttachmentStudentService attachmentStudentService, ModuleService moduleService) {
        this.certificateService = certificateService;
        this.attachmentStudentService = attachmentStudentService;
        this.moduleService = moduleService;
    }
    /*@GetMapping("/last-module")
    public RecommendModuleNewProjection getRecommendedLastModule(){
        return this.attachmentStudentService.getRecommendedModuleLast();
    }*/

    @GetMapping("/lastVideos")
    public Page<AttachmentProjection> getLastVideos(@ParameterObject Pageable pageable) {
        return attachmentStudentService.getLastUpdatedAttachment(pageable);
    }

    @GetMapping("/get-required/content")
    public TemporaryRequiredContentProjection getRequiredContent() {
        return attachmentStudentService.getRequiredContentProjection();
    }

    /*@GetMapping("/certificates")
    public List<CertificateProjection> getLastThreeCertificates() {
        return this.certificateService.getLastThreeCertificates();
    }*/
    @GetMapping("/modules")
    public List<ModuleUserProjection> getLastModules() {
        return moduleService.getModulesByUserWithStatus();
    }
}
