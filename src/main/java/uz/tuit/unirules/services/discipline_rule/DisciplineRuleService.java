package uz.tuit.unirules.services.discipline_rule;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;
import uz.tuit.unirules.mapper.DisciplineRuleMapper;
import uz.tuit.unirules.projections.DisciplineRuleProjection;
import uz.tuit.unirules.repository.DisciplineRuleRepository;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisciplineRuleService implements
        SimpleCrud<Long, CreateDisciplineRuleReqDto, UpdateDisciplineRuleReqDto, DisciplineRuleRespDto> {
    private final DisciplineRuleRepository disciplineRuleRepository;
    private final DisciplineRuleMapper disciplineRuleMapper;
    private final AttachmentService attachmentService;

    public DisciplineRuleService(DisciplineRuleRepository disciplineRuleRepository, DisciplineRuleMapper disciplineRuleMapper, AttachmentService attachmentService) {
        this.disciplineRuleRepository = disciplineRuleRepository;
        this.disciplineRuleMapper = disciplineRuleMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    @Transactional
    public ApiResponse<DisciplineRuleRespDto> create(CreateDisciplineRuleReqDto createDisciplineRuleReqDto) {
        Attachment attachment = attachmentService.findById(createDisciplineRuleReqDto.attachmentId());
        DisciplineRule disciplineRule = DisciplineRule.builder()
                .title(createDisciplineRuleReqDto.title())
                .body(createDisciplineRuleReqDto.body())
                .attachment(attachment)
                .build();
        disciplineRuleRepository.save(disciplineRule);
        return new ApiResponse<>(
                201,
                "DisciplineRule is saved",
                true,
                disciplineRuleMapper.toDto(disciplineRule));
    }

    @Override
    public ApiResponse<DisciplineRuleRespDto> get(Long entityId) {
        DisciplineRuleProjection disciplineRuleProjection = disciplineRuleRepository
                .findDisciplineRuleById(entityId).
                orElseThrow(() ->
                        new EntityNotFoundException(
                                "discipline rule is not found by id = %s".formatted(entityId)));
        DisciplineRuleRespDto respDto = makeDisciplineRuleFromProjection(disciplineRuleProjection);
        return new ApiResponse<>(
                200,
                "discipline rule is found",
                true,
                respDto
        );
    }

    private static DisciplineRuleRespDto makeDisciplineRuleFromProjection(DisciplineRuleProjection projection) {
        return new DisciplineRuleRespDto(
                projection.getTitle(),
                projection.getBody(),
                projection.getAttachmentId());
    }

    public DisciplineRule findDisciplineRuleById(Long entityId) {
        return disciplineRuleRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Discipline Rule is not found by id = %s".formatted(entityId)));
    }

    @Override
    @Transactional
    public ApiResponse<DisciplineRuleRespDto> update(
            Long entityId, UpdateDisciplineRuleReqDto updateDisciplineRuleReqDto) {
        // discipline rule
        DisciplineRule disciplineRule = findDisciplineRuleById(entityId);
        //attachment
        Attachment attachment = attachmentService.findById(updateDisciplineRuleReqDto.attachmentId());
        try {
            disciplineRule.setTitle(updateDisciplineRuleReqDto.title());
            disciplineRule.setBody(updateDisciplineRuleReqDto.body());
            disciplineRule.setAttachment(attachment);
            disciplineRuleRepository.save(disciplineRule);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse<>(
                200,
                "discipline rule muvaffaqiyatli update boldi",
                true,
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<DisciplineRuleRespDto> delete(Long entityId) {
        DisciplineRule disciplineRule = findDisciplineRuleById(entityId);
        disciplineRule.setIsDeleted(true);
        disciplineRuleRepository.save(disciplineRule);
        return new ApiResponse<>(
                200,
                "discipline rule deleted",
                true,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<DisciplineRuleRespDto>> getAll() {
        List<DisciplineRuleRespDto> respDtoList = disciplineRuleRepository
                .findAllDisciplineRules(false).stream()
                .map(DisciplineRuleService::makeDisciplineRuleFromProjection).toList();
        return new ApiResponse<>(
                200,
                "all discipline rules",
                true,
                respDtoList

        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<DisciplineRuleRespDto>> getAllPagination(Pageable pageable) {
        // discipline rules hammasi pagega keladi
        Page<DisciplineRule> disciplineRulePage = disciplineRuleRepository.findAll(pageable);

        // discipline rule isdeleted false bolganlarni hammasi listga yigiladi
        List<DisciplineRule> disciplineRuleList = disciplineRulePage.getContent().stream().filter(
                disciplineRule -> disciplineRule.getIsDeleted().equals(false)).toList();

        // disciplinerulerespdto list ochiladi
        List<DisciplineRuleRespDto> respDtoList = new ArrayList<>();

        // discipline rule list discipline rule respdto ga convert boladi.
        disciplineRuleList.forEach(disciplineRule -> respDtoList.add(new DisciplineRuleRespDto(
                disciplineRule.getTitle(),
                disciplineRule.getBody(),
                disciplineRule.getAttachment().getId()
        )));
        return new ApiResponse<>(
                200,
                "all discipline rules pages",
                true,
                findAllPage(pageable).map(disciplineRuleMapper::toDto).toList()
        );
    }

    public Page<DisciplineRule> findAllPage(Pageable pageable) {
        return disciplineRuleRepository.findAll(pageable);
    }
}
