package uz.tuit.unirules.services.content_student;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.ContentStudentRepository;
import uz.tuit.unirules.services.AuthUserService;

import java.util.List;
import java.util.Optional;

@Service
public class ContentStudentService {
    private final ContentStudentRepository contentStudentRepository;
    private final AuthUserService authUserService;

    public ContentStudentService(ContentStudentRepository contentStudentRepository, AuthUserService authUserService) {
        this.contentStudentRepository = contentStudentRepository;
        this.authUserService = authUserService;
    }

    public ContentStudent buildContentStudent(Content content, User user) {
        return ContentStudent.builder()
                .status(ContentStudent.UserContentStatus.NOT_STARTED)
                .content(content)
                .isDeleted(false)
                .user(user)
                /*.isRead(false)*/
                .isRequired(content.getIsRequired())
                .build();
    }

    @Transactional
    public void saveAll(List<ContentStudent> contentStudents) {
        contentStudentRepository.saveAll(contentStudents);
    }

   /* public ContentStudent checkIfCreateByStudentId(Long authUserId, Long attachmentId) {
        Optional<ContentStudent> contentStudentOptional = contentStudentRepository.findByUserIdAndAttachmentId(authUserId, attachmentId);
        return contentStudentOptional.orElseGet(() -> contentStudentRepository.save(ContentStudent.builder()
                .content(contentStudentRepository.findContentByAttachmentId(attachmentId).orElseThrow())
                .user(authUserService.getAuthUser())
                .build()));
    }*/
}
