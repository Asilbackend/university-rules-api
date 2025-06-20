package uz.tuit.unirules.services.content_student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.ContentStudentRepository;

import java.util.List;

@Service
public class ContentStudentService {
    private final ContentStudentRepository contentStudentRepository;

    public ContentStudentService(ContentStudentRepository contentStudentRepository) {
        this.contentStudentRepository = contentStudentRepository;
    }

    public ContentStudent buildContentStudent(Content content, User user) {
        return ContentStudent.builder()
                .status(ContentStudent.UserContentStatus.NOT_STARTED)
                .content(content)
                .isDeleted(false)
                .user(user)
                .isRead(false)
                .isRequired(content.getIsRequired())
                .build();
    }

    @Transactional
    public void saveAll(List<ContentStudent> contentStudents) {
        contentStudentRepository.saveAll(contentStudents);
    }
}
