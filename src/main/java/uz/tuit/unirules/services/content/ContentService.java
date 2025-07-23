package uz.tuit.unirules.services.content;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.comment.CommentRepository;
import uz.tuit.unirules.entity.content.*;

import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.handler.exceptions.AlreadyExist;


import uz.tuit.unirules.repository.*;
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
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;


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

    private List<ContentElement> getContentElementsUnsaved(ContentCreateDto contentCreateDto, Content content) {
        List<ContentElement> contentElementsUnsaved = new ArrayList<>();
        List<ContentCreateDto.AttachmentElement> attachmentElements = contentCreateDto.attachmentElements();
        attachmentElements.forEach(attachmentElement -> {
            ContentElement build = ContentElement.builder()
                    .attachment(attachmentRepository.findById(attachmentElement.attachmentId()).orElseThrow())
                    .title(attachmentElement.title())
                    .content(content)
                    .orderElement(attachmentElement.orderElement())
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

    private List<ContentElement> createdContentElements(Map<Long, String> attachmentsAndTitles, Map<String, String> titlesAndBodies, Content content) {
        int i = 0;

        List<ContentElement> unsavedContentElements = new ArrayList<>();
        for (Long attachId : attachmentsAndTitles.keySet()) {

            Attachment attachment = attachmentRepository.findById(attachId)
                    .orElseThrow(() -> new EntityNotFoundException("Attachment not found: " + attachId));
            String title = attachmentsAndTitles.get(attachId);
            if (contentElementRepository.existsByTitle(title)) {
                throw new AlreadyExist("the title mus tbe unique");
            }
            attachment.setTitle(title);
            attachmentRepository.save(attachment);
            ContentElement contentElementBuilt = ContentElement.builder()
                    .orderElement(i++)
                    .title(title)
                    .attachment(attachment)
                    .content(content)
                    .build();
            unsavedContentElements.add(contentElementBuilt);
        }
        for (String title : titlesAndBodies.keySet()) {
            if (contentElementRepository.existsByTitle(title)) {
                throw new AlreadyExist("the title mus tbe unique");
            }
            ContentElement contentElementBuilt = ContentElement.builder()
                    .orderElement(i++)
                    .title(title)
                    .text(titlesAndBodies.get(title))
                    .content(content)
                    .build();
            unsavedContentElements.add(contentElementBuilt);
        }
        return unsavedContentElements;
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
        /*List<ContentElement> contentElementsUnsaved = new ArrayList<>();
        List<ContentCreateDto.AttachmentElement> attachmentElements = contentCreateDto.attachmentElements();
        attachmentElements.forEach(attachmentElement -> {
            ContentElement build = ContentElement.builder()
                    .attachment(attachmentRepository.findById(attachmentElement.attachmentId()).orElseThrow())
                    .title(attachmentElement.title())
                    .content(content)
                    .orderElement(attachmentElement.orderElement())
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
        });*/
        List<ContentElement> contentElementsUnsaved = getContentElementsUnsaved(contentCreateDto, content);
        Module module = moduleService.findById(contentCreateDto.moduleId());
        content.setModule(module);
        content.setTitle(contentCreateDto.contentTitle());
        content.setIsRequired(module.getModuleState().equals(Module.ModuleState.REQUIRED) || contentCreateDto.required());
        content.getContentElements().clear();
        entityManager.flush();
        content.getContentElements().addAll(contentElementsUnsaved);
        contentRepository.save(content);


        /*Content content = findContentById(contentId);
        if (!Objects.equals(content.getModule().getId(), contentCreateDto.moduleId())) {
            Module module = moduleService.findById(contentCreateDto.moduleId());
            content.setModule(module);
            content.setTitle(contentCreateDto.contentTitle());
        }

        // 2. Mavjud elementlar va yangi ma'lumotlarni olish
        List<ContentElement> existingElements = content.getContentElements();
        Map<String, String> titlesAndBodies = contentCreateDto.titlesAndBodies();
        Map<Long, String> attachmentsAndTitles = contentCreateDto.attachmentsAndTitles();

        // 3. Yangi va mavjud titleni birlashtirish
        Set<String> incomingTitles = new HashSet<>(titlesAndBodies.keySet());
        incomingTitles.addAll(attachmentsAndTitles.values());

        // 4. Mavjud elementlarni yangilash va yangi elementlarni yaratish
        List<ContentElement> updatedElements = new ArrayList<>();
        int orderStart = existingElements.size(); // Yangi elementlar uchun tartib raqami

        for (String title : incomingTitles) {
            // Mavjud elementni izlash
            Optional<ContentElement> existingElement = existingElements.stream()
                    .filter(e -> e.getTitle().equals(title))
                    .findFirst();

            if (existingElement.isPresent()) {
                // Mavjud elementni yangilash
                ContentElement element = existingElement.get();
                String newText = titlesAndBodies.get(title);
                if (newText != null) {
                    element.setText(newText); // Text ni yangilash
                }
                updatedElements.add(element);
            } else {
                // Yangi element yaratish
                String text = titlesAndBodies.get(title);
                Attachment attachment = attachmentsAndTitles.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(title))
                        .findFirst()
                        .map(entry -> attachmentRepository.findById(entry.getKey())
                                .orElseThrow(() -> new EntityNotFoundException("Attachment topilmadi: " + entry.getKey())))
                        .orElse(null);

                ContentElement newElement = ContentElement.builder()
                        .title(title)
                        .text(text)
                        .attachment(attachment)
                        .orderElement(++orderStart)
                        .content(content)
                        .build();
                updatedElements.add(newElement);
            }
        }

        // 5. Content ni yangi elementlar bilan yangilash
        content.getContentElements().clear();
        content.getContentElements().addAll(updatedElements);
        *//* content.setContentElements(updatedElements);*//*
        // 6. Saqlash (orphanRemoval avtomatik ravishda keraksiz elementlarni o'chiradi)
        contentRepository.save(content);*/
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
    public ApiResponse<List<?>> getAll(Boolean isDeleted) {
        List<Content> allContents;
        if (isDeleted == null) {
            allContents = contentRepository.findAll();
        } else {
            allContents = contentRepository.findAllByIsDeleted(isDeleted);
        }
        return new ApiResponse<>(200, "contents", true, allContents);
    }

   /*@Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ContentRespRecordDto>> getAllPagination(Pageable pageable) {
        Page<Content> all = contentRepository.findAll(pageable);
        List<Content> list = all.getContent().stream().filter(content -> content.getIsDeleted().equals(false)).toList();
        List<ContentRespRecordDto> contentRespDtos = new ArrayList<>();
        list.forEach(content ->
                contentRespDtos.add(new ContentRespRecordDto(content.getId(), content.getTitle(), content.getBody(), getLongStream(content), content.getModule().getId(), content.getAverageContentRating())
                ));
        return new ApiResponse<>(200, "Contents", true, contentRespDtos);
    }*/

    private static List<Long> getLongStream(Content content) {
        /*return content.getAttachments().stream().map(BaseEntity::getId).toList();*/
        return null;
    }


    public List<?> getAllByModuleId(Long moduleId) {
        /*return  contentRepository.findAllByModuleIdAndUserId(moduleId,authUserService.getAuthUserId());*/
        return contentJdbcRepository.getContentsByModuleIdForPage(moduleId, authUserService.getAuthUserId());
    }

    @Transactional
    public String getTextByTitle(String title) {

        return contentElementRepository.findTextByTitle(title).orElseThrow(() -> new EntityNotFoundException("not found by title: %s".formatted(title)));
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
            unsavedContentElementStudents.add(
                    contentElementStudentRepository.save(
                            ContentElementStudent.builder()
                                    .contentElement(contentElement)
                                    .student(user)
                                    .isRead(false)
                                    .build()
                    )
            );
        }
        contentElementStudentRepository.saveAll(unsavedContentElementStudents);
    }

    private ContentStudent findIfCreateContentStudent(Long contentId) {
        boolean isRequired = moduleRepository.isRequiredContentByContentId(contentId);
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

    public List<ContentStudent> findByContentIdAndUserId(Long contentId, Long authUserId) {
        Optional<ContentStudent> contentStudentOptional = contentStudentRepository.findByContentIdAndUserId(contentId, authUserId);
        return contentStudentOptional.map(List::of).orElseGet(ArrayList::new);
    }

    public void readContentElementFromContent(Long contentElementId) {
        ContentElementStudent contentElementStudent1 = contentElementStudentRepository.findByContentElementIdAndStudentId(contentElementId, authUserService.getAuthUserId()).orElseThrow();
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
