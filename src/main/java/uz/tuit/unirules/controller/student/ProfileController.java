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
import uz.tuit.unirules.services.user.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/profile")
public class ProfileController {
    private final UserService userService;
    private final CertificateService certificateService;
    private final AttachmentStudentService attachmentStudentService;
    private final ModuleService moduleService;

    public ProfileController(UserService userService, CertificateService certificateService, AttachmentStudentService attachmentStudentService, ModuleService moduleService) {
        this.userService = userService;
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

    @GetMapping("/get-student-data")
    public Map<String, Object> getStudentData() {
        return userService.getStudentData();
    }

    /*@GetMapping("/certificates")
    public List<CertificateProjection> getLastThreeCertificates() {
        return this.certificateService.getLastThreeCertificates();
    }*/
    @GetMapping("/modules")
    public List<ModuleUserProjection> getLastModules() {
        return moduleService.getModulesByUserWithStatus();
    }

    @GetMapping("/filter-by-status")
    public List<?> getFilteredModules(@RequestParam UserModuleStatus userModuleStatus) {
        return moduleService.getFilteredModules(userModuleStatus);
    }

    public enum UserModuleStatus {
        FAILED, IN_PROGRESS, COMPLETED, NOT_STARTED
    }
}
