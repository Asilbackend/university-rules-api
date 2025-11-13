package uz.tuit.unirules.services.content;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.controller.student.ContentRespRecordDto;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content.*;

import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;

import uz.tuit.unirules.projections.FuzzySearchProjection;
import uz.tuit.unirules.repository.*;
import uz.tuit.unirules.projections.ContentRespProjection;
import uz.tuit.unirules.repository.AttachmentRepository;
import uz.tuit.unirules.repository.ContentRepository;
import uz.tuit.unirules.repository.ContentStudentRepository;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.services.AuthUserService;

import uz.tuit.unirules.services.module.ModuleService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ModuleService moduleService;
    private final AttachmentRepository attachmentRepository;
    private final ContentElementRepository contentElementRepository;
    private final AuthUserService authUserService;
    private final ContentStudentRepository contentStudentRepository;
    private final ModuleRepository moduleRepository;
    private final ContentElementStudentRepository contentElementStudentRepository;
    private final ContentJdbcRepository contentJdbcRepository;
    private final EntityManager entityManager;
    private final NewsRepository newsRepository;


    @Transactional
    public ApiResponse<?> create(ContentCreateDto contentCreateDto) {
        validateContentCreateDto(contentCreateDto);
        boolean requiredContent = contentCreateDto.required();
        // 1. Modulni olish
        Module module = moduleService.findById(contentCreateDto.moduleId());
        if (module.getModuleState().equals(Module.ModuleState.REQUIRED)) {
            requiredContent = true;
        }
        Content content = Content.builder()
                .title(contentCreateDto.contentTitle())
                .module(module)
                .isRequired(requiredContent)
                .build();
        List<ContentElement> contentElementsUnsaved = getContentElementsUnsaved(contentCreateDto, content);
        content.setModule(moduleService.findById(contentCreateDto.moduleId()));
        content.setTitle(contentCreateDto.contentTitle());
        content.setContentElements(contentElementsUnsaved);
        contentRepository.save(content);
        return new ApiResponse<>(201, "Content is saved", true, null);
    }


    private List<Attachment> getAttachmentsByIds(List<Long> attachmentedIds) {
        return attachmentedIds == null || attachmentedIds.isEmpty()
                ? List.of()
                : attachmentRepository.findAllById(attachmentedIds);
    }


    public HttpEntity<?> get(Long entityId) {
        Content content = findContentById(entityId);
        ContentRespDto contentRespDto = new ContentRespDto(
                content.getId(),
                content.getTitle(),
                content.getModule(),
                content.getIsRequired(),
                content.getAverageContentRating(),
                content.getIsDeleted()
        );
        return ResponseEntity.ok(contentRespDto);
    }

    @Transactional
    public void update(Long contentId, ContentCreateDto contentCreateDto) {
        validateContentCreateDto(contentCreateDto);
        //check title unique
        Content content = findContentById(contentId);
        List<ContentElement> contentElementsUnsaved = getContentElementsUnsaved(contentCreateDto, content);
        Module module = moduleService.findById(contentCreateDto.moduleId());
        content.setModule(module);
        content.setTitle(contentCreateDto.contentTitle());
        content.setIsRequired(module.getModuleState().equals(Module.ModuleState.REQUIRED) || contentCreateDto.required());
        content.getContentElements().clear();
        entityManager.flush();
        content.getContentElements().addAll(contentElementsUnsaved);
        contentRepository.save(content);
    }

    private List<ContentElement> getContentElementsUnsaved(ContentCreateDto contentCreateDto, Content content) {
        List<ContentElement> contentElementsUnsaved = new ArrayList<>();
        List<ContentCreateDto.AttachmentElement> attachmentElements = contentCreateDto.attachmentElements();
        attachmentElements.forEach(attachmentElement -> {
            Attachment attachment = attachmentRepository.findById(attachmentElement.attachmentId()).orElseThrow();
            String title = attachmentElement.title();
            attachment.setTitle(title);
            attachmentRepository.save(attachment);
            ContentElement build = ContentElement.builder()
                    .attachment(attachment)
                    .title(title)
                    .content(content)
                    .orderElement(attachmentElement.orderElement())
                    .description(attachmentElement.description())
                    .build();
            contentElementsUnsaved.add(build);

        });
        List<ContentCreateDto.TextElement> textElements = contentCreateDto.textElements();
        textElements.forEach(textElement -> {
            ContentElement build = ContentElement.builder()
                    .title(textElement.title())
                    .content(content)
                    .orderElement(textElement.orderElement())
                    .text(textElement.body())
                    .build();
            contentElementsUnsaved.add(build);
        });
        return contentElementsUnsaved;
    }


    public void validateContentCreateDto(ContentCreateDto dto) {
        List<String> titles = new ArrayList<>();
        // TextElement title'larini yigish
        for (ContentCreateDto.TextElement textElement : dto.textElements()) {
            if (textElement.title() != null) {
                titles.add(textElement.title().trim());
            }
        }

        // AttachmentElement title'larini yigish
        for (ContentCreateDto.AttachmentElement attachmentElement : dto.attachmentElements()) {
            if (attachmentElement.title() != null) {
                titles.add(attachmentElement.title().trim());
            }
        }

        // Uniqueness tekshirish
        Set<String> uniqueTitles = new HashSet<>(titles);
        if (uniqueTitles.size() != titles.size()) {
            throw new IllegalArgumentException("TextElement va AttachmentElement title'lari unique bo'lishi kerak.");
        }
    }


    @Transactional
    public Content findContentWithFetchById(Long entityId) {
        return contentRepository.findByIdFetch(entityId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(entityId)));
        /*return contentRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(entityId)));*/
    }

    public Content findContentById(Long contentId) {
        return contentRepository.findById(contentId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(contentId)));
    }


    @Transactional
    public ApiResponse<?> delete(Long entityId) {
        Content content = findContentById(entityId);
        content.setIsDeleted(true);
        contentRepository.save(content);
        return new ApiResponse<>(
                200, "deleted", true, null
        );
    }


    @Transactional(readOnly = true)
    public ApiResponse<Set<?>> getAll(Boolean isDeleted) {
        List<Content> allContents;
        if (isDeleted == null) {
            allContents = contentRepository.findAll();
        } else {
            allContents = contentRepository.findAllByIsDeleted(isDeleted);
        }
        Set<Map<String, Object>> resp = new LinkedHashSet<>();
        for (Content allContent : allContents) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", allContent.getTitle());
            map.put("averageRating", allContent.getAverageContentRating());
            map.put("moduleId", allContent.getModule().getId());
            map.put("required", allContent.getIsRequired());
            map.put("delete", allContent.getIsDeleted());
            resp.add(map);
        }
        return new ApiResponse<>(200, "contents", true, resp);
    }


    public List<ContentRespProjection> getAllByModuleIdProjection(Long moduleId) {
        return contentRepository.findAllByModuleIdAndUserId(moduleId, authUserService.getAuthUserId());
    }

    @Transactional(readOnly = true)
    public ContentCreateDto getById(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found with id: " + contentId));

        // ContentElementlarni ikki turga ajratish
        List<ContentCreateDto.TextElement> textElements = new ArrayList<>();
        List<ContentCreateDto.AttachmentElement> attachmentElements = new ArrayList<>();

        for (ContentElement element : content.getContentElements()) {
            if (element.getText() != null) {
                textElements.add(new ContentCreateDto.TextElement(
                        element.getTitle(),
                        element.getText(),
                        element.getOrderElement()
                ));
            } else if (element.getAttachment() != null) {
                attachmentElements.add(new ContentCreateDto.AttachmentElement(
                        element.getTitle(),
                        element.getAttachment().getId(),
                        element.getOrderElement(),
                        element.getDescription()
                ));
            }
        }

        return new ContentCreateDto(
                content.getModule().getId(),
                content.getTitle(),
                content.getIsRequired(),
                textElements,
                attachmentElements
        );
    }


    public List<ContentRespRecordDto> getAllByModuleId(Long moduleId) {
        /*return  contentRepository.findAllByModuleIdAndUserId(moduleId,authUserService.getAuthUserId());*/
        return contentJdbcRepository.getContentsByModuleIdForPage(moduleId, authUserService.getAuthUserId());


    }

    @Transactional
    public String getTextByTitle(String title) {
        return contentElementRepository.findTextByTitle(title, false).orElseThrow(() -> new EntityNotFoundException("not found by title: %s".formatted(title)));
    }

    @Transactional
    public void startContent(Long contentId) {
        ContentStudent contentStudent = findIfCreateContentStudent(contentId);
        contentStudent.setStatus(ContentStudent.UserContentStatus.IN_PROGRESS);
        contentStudentRepository.save(contentStudent);
        createContentElementStudents(contentId, contentStudent.getUser());
    }

    private void createContentElementStudents(Long contentId, User user) {
        List<ContentElementStudent> unsavedContentElementStudents = new ArrayList<>();
        List<ContentElement> contentElements = contentElementRepository.findAllByContentId(contentId);
        for (ContentElement contentElement : contentElements) {
            Optional<ContentElementStudent> elementStudentOptional = contentElementStudentRepository.findByContentElementIdAndStudentId(contentElement.getId(), user.getId());
            if (elementStudentOptional.isPresent()) continue;
            unsavedContentElementStudents.add(
                    ContentElementStudent.builder()
                            .contentElement(contentElement)
                            .student(user)
                            .isRead(false)
                            .build()
            );
        }
        contentElementStudentRepository.saveAll(unsavedContentElementStudents);
    }

    private ContentStudent findIfCreateContentStudent(Long contentId) {
        boolean isRequired = moduleRepository.isRequiredContentByContentId(contentId);// contentni moduli requied bolsa contentstudent ham required boladi
        User authUser = authUserService.getAuthUser();
        List<ContentStudent> contentStudents = findByContentIdAndUserId(contentId, authUser.getId());
        if (contentStudents.isEmpty()) {
            return contentStudentRepository.save(
                    ContentStudent.builder()
                            .user(authUser)
                            .status(ContentStudent.UserContentStatus.NOT_STARTED)
                            .content(findContentById(contentId))
                            .startedAt(LocalDateTime.now())
                            .isRequired(isRequired)
                            .build()
            );
        } else {
            return contentStudents.getLast();
        }
    }


    @Transactional(readOnly = true)
    public List<FuzzySearchProjection> fuzzySearchContentModuleContentElementByTitle(String title) {
        return contentRepository.fuzzySearch(title);
    }

    public List<ContentStudent> findByContentIdAndUserId(Long contentId, Long authUserId) {
        Optional<ContentStudent> contentStudentOptional = contentStudentRepository.findByContentIdAndUserId(contentId, authUserId);
        return contentStudentOptional.map(List::of).orElseGet(ArrayList::new);
    }

    @Transactional
    public void readContentElementFromContent(Long contentElementId) {

        ContentElementStudent contentElementStudent1 = contentElementStudentRepository.findByContentElementIdAndStudentId(contentElementId, authUserService.getAuthUserId()).orElseThrow();
        if (Objects.equals(contentElementStudent1.getIsRead(), true)) {
            return;
        }
        contentElementStudent1.setIsRead(true);
        contentElementStudentRepository.save(contentElementStudent1);
        Content content = contentElementStudent1.getContentElement().getContent();

        Integer countRead = contentElementStudentRepository.countByIsReadTrue(content.getId(), authUserService.getAuthUserId());
        Integer all = contentElementRepository.countByContentId(content.getId());
        if (countRead > 0 && Objects.equals(countRead, all)) {
            ContentStudent contentStudent = findByContentIdAndUserId(content.getId(), authUserService.getAuthUserId()).getLast();
            contentStudent.setStatus(ContentStudent.UserContentStatus.COMPLETED);
            contentStudent.setReadAt(LocalDateTime.now());
            contentStudentRepository.save(contentStudent);
        }
    }
}
