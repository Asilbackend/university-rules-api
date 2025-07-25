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
    private final ContentRepository contentRepository;


    @Transactional
    public void updateVideoPercent(Long authUserId, Long attachmentId, Double percent, Long contentId) {
        AttachmentStudent attachmentStudent = findIfCreateAttachStudent(authUserId, attachmentId);
        if (attachmentStudent.getProgress() <= percent && !attachmentStudent.getIsRead()) {
            attachmentStudent.setProgress(percent);
            if (percent <= 100.0 && percent >= 95.0) {
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

        calculateAndSetAverageRatingToContent(attachmentId);
        return ResponseEntity.noContent().build();
    }

    private void calculateAndSetAverageRatingToContent(Long attachmentId) {
        List<Content> contentList = contentElementRepository.findContentByAttachmentId(attachmentId);
        if (!contentList.isEmpty()) {
            Double averageRate = attachmentStudentRepository.calculateAverageRateByAttachmentId(attachmentId);
            contentList.forEach(content -> {
                content.setAverageContentRating(averageRate);
            });
            contentRepository.saveAll(contentList);
        }
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

    public Page<AttachmentProjection> getLastUpdatedAttachment(Pageable pageable) {
        return attachmentStudentRepository.findLastUpdatedAttachments(authUserService.getAuthUserId(), pageable);
    }
}
