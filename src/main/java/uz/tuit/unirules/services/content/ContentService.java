package uz.tuit.unirules.services.content;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.content.ContentElement;
import uz.tuit.unirules.entity.content.ContentElementRepository;

import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.handler.exceptions.AlreadyExist;

import uz.tuit.unirules.projections.ContentRespProjection;
import uz.tuit.unirules.repository.AttachmentRepository;
import uz.tuit.unirules.repository.ContentRepository;
import uz.tuit.unirules.repository.ContentStudentRepository;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.module.ModuleService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final ModuleService moduleService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentStudentService attachmentStudentService;
    private final ContentElementRepository contentElementRepository;
    private final AuthUserService authUserService;
    private final ContentStudentRepository contentStudentRepository;
    private final ModuleRepository moduleRepository;

    public ContentService(ContentRepository contentRepository, ModuleService moduleService, AttachmentRepository attachmentRepository, AttachmentStudentService attachmentStudentService,
                          ContentElementRepository contentElementRepository, AuthUserService authUserService,
                          ContentStudentRepository contentStudentRepository,
                          ModuleRepository moduleRepository) {
        this.contentRepository = contentRepository;
        this.moduleService = moduleService;
        this.attachmentRepository = attachmentRepository;
        this.attachmentStudentService = attachmentStudentService;
        this.contentElementRepository = contentElementRepository;
        this.authUserService = authUserService;
        this.contentStudentRepository = contentStudentRepository;
        this.moduleRepository = moduleRepository;
    }


    @Transactional
    public ApiResponse<ContentRespDto> create(ContentCreateDto contentCreateDto) {
        boolean requiredContent = false;
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
        List<ContentElement> unsavedCElements = createdContentElements(contentCreateDto.attachmentsAndTitles(), contentCreateDto.titlesAndBodies(), content);
        content.setContentElements(unsavedCElements);
        contentRepository.save(content);
        return new ApiResponse<>(201, "Content is saved", true, null);
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


    public ApiResponse<?> get(Long entityId) {
        Content content = findContentById(entityId);
        return new ApiResponse<>(200, "content", true, content);
    }

   /* private static ContentRespDto makeContentRespDtoFromProjection(ContentProjection contentProjection) {
        return new ContentRespDto(contentProjection.getId(), contentProjection.getTitle(), contentProjection.getBody(),
                contentProjection.getAttachmentIds(),
                contentProjection.getModuleId(),
                contentProjection.getAverageContentRating()
        );
    }*/


    @Transactional
    public ApiResponse<ContentRespDto> update(Long contentId, ContentCreateDto contentCreateDto) {
        // 1. Content ni topish
        Content content = findContentById(contentId);
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
                        .orderElement(orderStart++)
                        .content(content)
                        .build();
                updatedElements.add(newElement);
            }
        }

        // 5. Content ni yangi elementlar bilan yangilash
        content.setContentElements(updatedElements);
        // 6. Saqlash (orphanRemoval avtomatik ravishda keraksiz elementlarni o'chiradi)
        contentRepository.save(content);
        return new ApiResponse<>(200, "Kontent elementlari muvaffaqiyatli sinxronlashtirildi", true, null);
    }


    private Content findContentById(Long entityId) {
        return contentRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(entityId)));
    }


    @Transactional
    public ApiResponse<ContentRespDto> delete(Long entityId) {
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
    public ApiResponse<List<ContentRespDto>> getAllPagination(Pageable pageable) {
        Page<Content> all = contentRepository.findAll(pageable);
        List<Content> list = all.getContent().stream().filter(content -> content.getIsDeleted().equals(false)).toList();
        List<ContentRespDto> contentRespDtos = new ArrayList<>();
        list.forEach(content ->
                contentRespDtos.add(new ContentRespDto(content.getId(), content.getTitle(), content.getBody(), getLongStream(content), content.getModule().getId(), content.getAverageContentRating())
                ));
        return new ApiResponse<>(200, "Contents", true, contentRespDtos);
    }*/

    private static List<Long> getLongStream(Content content) {
        /*return content.getAttachments().stream().map(BaseEntity::getId).toList();*/
        return null;
    }


    public List<ContentRespProjection> getAllByModuleId(Long moduleId) {
        return contentRepository.findAllByModuleIdAndUserId(moduleId,authUserService.getAuthUserId());
    }

    @Transactional(readOnly = true)
    public String getTextByTitle(String title) {
        return contentElementRepository.findTextByTitle(title).orElseThrow(() -> new EntityNotFoundException("not found by title: %s".formatted(title)));
    }

    public void startContent(Long contentId) {
        ContentStudent contentStudent = findIfCreateContentStudent(contentId);
        contentStudent.setStatus(ContentStudent.UserContentStatus.IN_PROGRESS);
        contentStudentRepository.save(contentStudent);
    }

    private ContentStudent findIfCreateContentStudent(Long contentId) {
        boolean isRequired = moduleRepository.isRequiredContentByContentId(contentId);
        User authUser = authUserService.getAuthUser();
        List<ContentStudent> contentStudents = contentStudentRepository.findByContentIdAndUserId(contentId, authUser.getId());
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
}
