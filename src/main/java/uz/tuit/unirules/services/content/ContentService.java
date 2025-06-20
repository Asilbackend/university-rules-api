package uz.tuit.unirules.services.content;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.content.Content;
import uz.tuit.unirules.entity.content_student.AttachmentStudent;
import uz.tuit.unirules.entity.content_student.ContentStudent;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.projections.ContentProjection;
import uz.tuit.unirules.repository.AttachmentRepository;
import uz.tuit.unirules.repository.ContentRepository;
import uz.tuit.unirules.services.attachment_student.AttachmentStudentService;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService implements SimpleCrud<Long, ContentCreateDto, ContentCreateDto, ContentRespDto> {
    private final ContentRepository contentRepository;
    private final ModuleService moduleService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentStudentService attachmentStudentService;

    public ContentService(ContentRepository contentRepository, ModuleService moduleService, AttachmentRepository attachmentRepository, AttachmentStudentService attachmentStudentService) {
        this.contentRepository = contentRepository;
        this.moduleService = moduleService;
        this.attachmentRepository = attachmentRepository;
        this.attachmentStudentService = attachmentStudentService;
    }

    @Override
    @Transactional
    public ApiResponse<ContentRespDto> create(ContentCreateDto contentCreateDto) {
        Boolean requiredContent = contentCreateDto.isRequired();
        // 1. Modulni olish
        Module module = moduleService.findById(contentCreateDto.moduleId());
        if (module.getModuleState().equals(Module.ModuleState.REQUIRED)) {
            requiredContent = true;
        }
        // 2. Attachment ID'lar bo‘sh bo‘lmasligini tekshirish
        List<Attachment> attachments = getAttachmentsByIds(contentCreateDto.attachmentIds());

        // 3. Contentni yaratish (yana Attachmentlar keyin bog‘lanadi)
        Content content = Content.builder()
                .title(contentCreateDto.title())
                .body(contentCreateDto.body())
                .module(module)
                .isRequired(requiredContent)
                .averageContentRating(
                        contentCreateDto.averageContentRating() != null
                                ? contentCreateDto.averageContentRating()
                                : 0D
                )
                .build();

        // 4. Attachmentlarni content bilan bog‘lash (child tarafdan `ManyToOne`)
        attachments.forEach(attachment -> attachment.setContent(content));
        // 5. Contentga attachment'larni set qilish (parent tarafdan `OneToMany`)
        content.setAttachments(attachments);
        // 6. Contentni saqlash (CascadeType.ALL ishlaydi -> Attachmentlar ham saqlanadi)
        Content savedContent = contentRepository.save(content);
        // 7. DTO tayyorlash
        List<Long> attachmentIds = attachments.stream()
                .map(Attachment::getId)
                .toList();

        ContentRespDto dto = new ContentRespDto(
                savedContent.getId(),
                savedContent.getTitle(),
                savedContent.getBody(),
                attachmentIds,
                module.getId(),
                savedContent.getAverageContentRating()
        );
        attachmentStudentService.asyncCreateByAttachments(attachments, content);
        return new ApiResponse<>(200, "Content is saved", true, dto);
    }

    private List<Attachment> getAttachmentsByIds(List<Long> attachmentedIds) {
        return attachmentedIds == null || attachmentedIds.isEmpty()
                ? List.of()
                : attachmentRepository.findAllById(attachmentedIds);
    }


    @Override
    public ApiResponse<ContentRespDto> get(Long entityId) {
        ContentProjection contentProjection = contentRepository.findContentById(entityId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(entityId)));
        ContentRespDto contentRespDto = makeContentRespDtoFromProjection(contentProjection);
        return new ApiResponse<>(200, "content", true, contentRespDto);
    }

    private static ContentRespDto makeContentRespDtoFromProjection(ContentProjection contentProjection) {
        return new ContentRespDto(contentProjection.getId(), contentProjection.getTitle(), contentProjection.getBody(),
                contentProjection.getAttachmentIds(),
                contentProjection.getModuleId(),
                contentProjection.getAverageContentRating()
        );
    }

    @Override
    @Transactional
    public ApiResponse<ContentRespDto> update(Long entityId, ContentCreateDto contentCreateDto) {
        Module module = moduleService.findById(contentCreateDto.moduleId());
        List<Attachment> attachments = getAttachmentsByIds(contentCreateDto.attachmentIds());
        Content content = findContentById(entityId);
        try {
            content.setAverageContentRating(content.getAverageContentRating());
            content.setTitle(contentCreateDto.title());
            content.setBody(content.getBody());
            content.setAttachments(attachments);
            content.setModule(module);
            contentRepository.save(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse<>(201, "updated", true, null);
    }

    private Content findContentById(Long entityId) {
        return contentRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("not found by id = %s".formatted(entityId)));
    }

    @Override
    @Transactional
    public ApiResponse<ContentRespDto> delete(Long entityId) {
        Content content = findContentById(entityId);
        content.setIsDeleted(true);
        contentRepository.save(content);
        return new ApiResponse<>(
                200, "deleted", true, null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ContentRespDto>> getAll() {
        List<ContentRespDto> contentRespDtos = contentRepository.findAllContents(false).stream().map(ContentService::makeContentRespDtoFromProjection).toList();
        return new ApiResponse<>(200, "ContentRespDto", true, contentRespDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ContentRespDto>> getAllPagination(Pageable pageable) {
        Page<Content> all = contentRepository.findAll(pageable);
        List<Content> list = all.getContent().stream().filter(content -> content.getIsDeleted().equals(false)).toList();
        List<ContentRespDto> contentRespDtos = new ArrayList<>();
        list.forEach(content ->
                contentRespDtos.add(new ContentRespDto(content.getId(), content.getTitle(), content.getBody(), getLongStream(content), content.getModule().getId(), content.getAverageContentRating())
                ));
        return new ApiResponse<>(200, "Contents", true, contentRespDtos);
    }

    private static List<Long> getLongStream(Content content) {
        return content.getAttachments().stream().map(BaseEntity::getId).toList();
    }

    public ApiResponse<List<ContentRespDto>> getAllByModuleId(Long moduleId, Pageable pageable) {
        List<ContentRespDto> list = contentRepository.findAllByModuleId(moduleId, pageable).map(ContentService::makeContentRespDtoFromProjection).toList();
        return new ApiResponse<>(200, "Contents", true, list);
    }
}
