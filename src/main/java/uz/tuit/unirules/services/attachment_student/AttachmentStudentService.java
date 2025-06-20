package uz.tuit.unirules.services.attachment_student;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.dto.respond_dto.TopVideo;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.AttachmentStudentRepository;
import uz.tuit.unirules.repository.ContentRepository;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.content_student.ContentStudentService;

import java.util.*;

@Service
public class AttachmentStudentService {
    private final AuthUserService authUserService;
    private final AttachmentStudentRepository attachmentStudentRepository;
    private final UserRepository userRepository;
    private final ContentStudentService contentStudentService;
    private final TaskExecutor taskExecutor;
    private final ApplicationContext applicationContext;
    private final ContentRepository contentRepository;


    public AttachmentStudentService(AuthUserService authUserService, AttachmentStudentRepository attachmentStudentRepository,
                                    UserRepository userRepository, ContentStudentService contentStudentService,
                                    @Qualifier("taskExecutor") TaskExecutor contentTaskExecutor,
                                    ApplicationContext applicationContext,
                                    ContentRepository contentRepository) {
        this.authUserService = authUserService;
        this.attachmentStudentRepository = attachmentStudentRepository;
        this.userRepository = userRepository;
        this.contentStudentService = contentStudentService;
        this.taskExecutor = contentTaskExecutor;
        this.applicationContext = applicationContext;
        this.contentRepository = contentRepository;
    }


    @Transactional
    public void updateVideoPercent(Long authUserId, Long attachmentId, Double percent) {
        List<AttachmentStudent> attachmentStudents = attachmentStudentRepository.findByStudentIdAndAttachmentId(authUserId, attachmentId);
        if (attachmentStudents.isEmpty()) {
            throw new EntityNotFoundException("berilgan qiymatlar boyicha malumot topilmadi");
        }
        AttachmentStudent attachmentStudent = attachmentStudents.getLast();
        if (attachmentStudent.getProgress() <= percent) {
            attachmentStudent.setProgress(percent);
            attachmentStudentRepository.save(attachmentStudent);
        }
    }

    /*@Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void createByAttachments(List<Attachment> attachments, Content content) {
        List<User> users = userRepository.findAllByRole("STUDENT");
        List<AttachmentStudent> attachmentStudents = new ArrayList<>();
        for (User user : users) {
            attachments.forEach(attachment -> {
                AttachmentStudent build = AttachmentStudent.builder()
                        .contentStudent(contentStudentService.getSavedContentStudent(content, user))
                        .attachment(attachment)
                        .build();
                attachmentStudents.add(build);
            });
        }
        attachmentStudentRepository.saveAll(attachmentStudents);
    }*/


    public void asyncCreateByAttachments(List<Attachment> attachments, Content content) {
        taskExecutor.execute(() -> {
            // Proxy orqali chaqirish
            AttachmentStudentService self = applicationContext.getBean(AttachmentStudentService.class);
            self.createByAttachments(attachments, content);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createByAttachments(List<Attachment> attachments, Content content) {
        List<User> users = userRepository.findAllByRole("STUDENT");

        List<ContentStudent> contentStudents = new ArrayList<>();
        Map<Long, ContentStudent> userIdToContentStudent = new HashMap<>();

        for (User user : users) {
            ContentStudent cs = contentStudentService.buildContentStudent(content, user);
            contentStudents.add(cs);
            userIdToContentStudent.put(user.getId(), cs); // So we can match later
        }

        // Save all ContentStudent in one go
        contentStudentService.saveAll(contentStudents);

        // Now create AttachmentStudents
        List<AttachmentStudent> attachmentStudents = new ArrayList<>();
        for (User user : users) {
            ContentStudent contentStudent = userIdToContentStudent.get(user.getId());
            for (Attachment attachment : attachments) {
                AttachmentStudent as = AttachmentStudent.builder()
                        .attachment(attachment)
                        .contentStudent(contentStudent)
                        .build();
                attachmentStudents.add(as);
            }
        }
        // Save all AttachmentStudent in one go
        attachmentStudentRepository.saveAll(attachmentStudents);
    }

    public List<TopVideo> getTopVideos() {
        List<TopVideo> topVideos = new ArrayList<>();
        List<AttachmentStudent> allByRatingDesc = attachmentStudentRepository.findAllByRatingDesc();
        allByRatingDesc.forEach(attachmentStudent -> {
            Content content = null;
            String title = null;
            try {
                ContentStudent contentStudent = attachmentStudent.getContentStudent();
                content = contentStudent.getContent();
                title = content.getTitle();
                topVideos.add(new TopVideo(attachmentStudent.getAttachment().getId(),
                        attachmentStudent.getContentStudent().getContent().getId(),
                        attachmentStudent.getAttachment().getThumbnailImageUrl(),
                        title, content.getIsRequired()));

            } catch (Exception ignored) {
            }
        });
        return topVideos;
    }

    @Transactional//pessimistik lock ishlatildi
    public HttpEntity<?> ratingVideo(Integer videoRate, Long attachmentId) {
        validateRatingRange(videoRate); // step 1
        Long userId = authUserService.getAuthUserId();
        // step 2: attachmentStudent aniqlash
        List<AttachmentStudent> attachmentStudents = attachmentStudentRepository
                .findLatestByStudentIdAndAttachmentIdForUpdate(userId, attachmentId);
        if (attachmentStudents == null || attachmentStudents.isEmpty()) {
            throw new RuntimeException("AttachmentStudent topilmadi.");
        }
        AttachmentStudent attachmentStudent = attachmentStudents.getFirst();
        attachmentStudent.setRating(videoRate);
        // step 3: bazaga yozish
        attachmentStudentRepository.save(attachmentStudent);
        // step 4: content ni olib, reytingni qayta hisoblash
        Content content = attachmentStudent.getContentStudent().getContent();
        List<AttachmentStudent> ratedAttachmentStudents = attachmentStudentRepository
                .findByContentId(content.getId());
        double averageRating = calculateAverageRating(ratedAttachmentStudents);
        content.setAverageContentRating(averageRating);
        // step 5: saqlash
        contentRepository.save(content);
        return ResponseEntity.noContent().build();
    }

    // 💡 Rating 1–5 oralig‘ida bo‘lishi kerak
    private void validateRatingRange(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("videoRate 1 dan 5 gacha bo'lishi kerak!");
        }
    }

    // 💡 O‘rtacha reytingni hisoblash
    private double calculateAverageRating(List<AttachmentStudent> ratedList) {
        return ratedList.stream()
                .map(AttachmentStudent::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

}
