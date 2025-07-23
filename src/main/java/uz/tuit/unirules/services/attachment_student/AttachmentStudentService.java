package uz.tuit.unirules.services.attachment_student;


import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import uz.tuit.unirules.projections.TopVideoProjection;

import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content.*;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;

import uz.tuit.unirules.projections.TemporaryRequiredContentProjection;
import uz.tuit.unirules.repository.*;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment.AttachmentService;
import uz.tuit.unirules.services.content.ContentService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentStudentService {
    private final AuthUserService authUserService;
    private final AttachmentStudentRepository attachmentStudentRepository;
    private final AttachmentService attachmentService;
    private final ContentService contentService;
    private final ContentElementRepository contentElementRepository;
    private final ContentElementStudentRepository contentElementStudentRepository;


    @Transactional
    public void updateVideoPercent(Long authUserId, Long attachmentId, Double percent, Long contentId) {
        AttachmentStudent attachmentStudent = findIfCreateAttachStudent(authUserId, attachmentId);
        if (attachmentStudent.getProgress() <= percent && !attachmentStudent.getIsRead()) {
            attachmentStudent.setProgress(percent);
            if (percent == 100.0) {
                Long contentElementId = contentElementRepository.findContentElementIdByContentIdAndAttachmentId(contentId, attachmentId);
                contentService.readContentElementFromContent(contentElementId);
                attachmentStudent.setIsRead(true);
            }
            attachmentStudentRepository.save(attachmentStudent);
        }
    }

    public ContentElementStudent findByContentElementIdAndStudentId(Long authUserId, Long attachmentId, Long contentId) {
        Long contentElementId = contentElementRepository.findContentElementIdByContentIdAndAttachmentId(contentId, attachmentId);
        Optional<ContentElementStudent> contentElementStudentOptional = contentElementStudentRepository.findByContentElementIdAndStudentId(contentElementId, authUserId);
        return contentElementStudentOptional.orElseThrow();
    }

    private AttachmentStudent findIfCreateAttachStudent(Long authUserId, Long attachmentId) {
        List<AttachmentStudent> attachmentStudents = attachmentStudentRepository.findByStudentIdAndAttachmentId(authUserId, attachmentId);
        if (attachmentStudents.isEmpty()) {
            return attachmentStudentRepository.save(
                    AttachmentStudent.builder()
                            .student(authUserService.getAuthUser())
                            .attachment(attachmentService.findById(attachmentId))
                            .build()
            );
        } else {
            return attachmentStudents.getLast();
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
       /* taskExecutor.execute(() -> {
            // Proxy orqali chaqirish
            AttachmentStudentService self = applicationContext.getBean(AttachmentStudentService.class);
            self.createByAttachments(attachments, content);
        });*/
    }

    /*@Transactional(propagation = Propagation.REQUIRES_NEW)
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
                        .student(user)
                        .build();
                attachmentStudents.add(as);
            }
        }
        // Save all AttachmentStudent in one go
        attachmentStudentRepository.saveAll(attachmentStudents);
    }*/

    public Page<TopVideoProjection> getTopVideos(Pageable pageable) {
        return attachmentStudentRepository.findAllTopVideos(Attachment.AttachType.VIDEO.toString(), pageable);
    }

    @Transactional
    public HttpEntity<?> ratingVideo(Integer videoRate, Long attachmentId) {
        validateRatingRange(videoRate); // step 1
        Long userId = authUserService.getAuthUserId();
        AttachmentStudent attachmentStudent = findIfCreateAttachStudent(userId, attachmentId);
        attachmentStudent.setRating(videoRate);
        attachmentStudentRepository.save(attachmentStudent);
        return ResponseEntity.noContent().build();
    }

    private void validateRatingRange(Integer rating) {
        if (rating == null || rating < 1 || rating > 5)
            throw new IllegalArgumentException("videoRate 1 dan 5 gacha bo'lishi kerak!");
    }


    private double calculateAverageRating(List<AttachmentStudent> ratedList) {// 💡 O‘rtacha reytingni hisoblash
        return ratedList.stream()
                .map(AttachmentStudent::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Transactional(readOnly = true)
    public TemporaryRequiredContentProjection getRequiredContentProjection() {
        return attachmentStudentRepository.findLastRequiredContentPro(authUserService.getAuthUserId());
    }

  /*  public void setComment(Long attachmentId, String comment) {
        AttachmentStudent attachmentStudent = findIfCreateAttachStudent(attachmentId, authUserService.getAuthUserId());
        attachmentStudent.setComment(comment);
        attachmentStudentRepository.save(attachmentStudent);
    }*/

    public Page<AttachmentProjection> getLastUpdatedAttachment(Pageable pageable) {
        return attachmentStudentRepository.findLastUpdatedAttachments(authUserService.getAuthUserId(), pageable);
    }
}
