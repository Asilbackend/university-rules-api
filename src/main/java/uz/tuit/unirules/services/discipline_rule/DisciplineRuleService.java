package uz.tuit.unirules.services.discipline_rule;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;
import uz.tuit.unirules.mapper.DisciplineRuleMapper;
import uz.tuit.unirules.repository.DisciplineRuleRepository;

import java.util.List;

@Service
public class DisciplineRuleService implements
        SimpleCrud<Long, CreateDisciplineRuleReqDto, UpdateDisciplineRuleReqDto, DisciplineRuleRespDto> {
    private final DisciplineRuleRepository disciplineRuleRepository;
    private final DisciplineRuleMapper disciplineRuleMapper;

    public DisciplineRuleService(DisciplineRuleRepository disciplineRuleRepository, DisciplineRuleMapper disciplineRuleMapper) {
        this.disciplineRuleRepository = disciplineRuleRepository;
        this.disciplineRuleMapper = disciplineRuleMapper;
    }

    @Override
    public ApiResponse<DisciplineRuleRespDto> create(CreateDisciplineRuleReqDto createDisciplineRuleReqDto) {
        DisciplineRule disciplineRule = DisciplineRule.builder()
                .title(createDisciplineRuleReqDto.title())
                .body(createDisciplineRuleReqDto.body())
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
        DisciplineRule disciplineRule = findById(entityId);
        return new ApiResponse<>(
                200,
                "discipline rule muvaffaqiyatli topildi",
                true,
                disciplineRuleMapper.toDto(disciplineRule)
        );
    }

    public DisciplineRule findById(Long entityId) {
        return disciplineRuleRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("bunday discipline rule mavjud emas"));
    }

    @Override
    public ApiResponse<DisciplineRuleRespDto> update(
            Long entityId, UpdateDisciplineRuleReqDto updateDisciplineRuleReqDto) {
        DisciplineRule disciplineRule = DisciplineRule.builder()
                .title(updateDisciplineRuleReqDto.title())
                .body(updateDisciplineRuleReqDto.body())
                .build();
        disciplineRuleRepository.save(disciplineRule);
        return new ApiResponse<>(
                200,
                "discipline rule muvaffaqiyatli update boldi",
                true,
                disciplineRuleMapper.toDto(disciplineRule)
        );
    }

    @Override
    public ApiResponse<DisciplineRuleRespDto> delete(Long entityId) {
        DisciplineRule disciplineRule = findById(entityId);
        disciplineRuleRepository.delete(disciplineRule);
        return new ApiResponse<>(
                200,
                "discipline rule deleted",
                true,
                null
        );
    }

    @Override
    public ApiResponse<List<DisciplineRuleRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all discipline rules",
                true,
                disciplineRuleRepository.findAll()
                        .stream().map(disciplineRuleMapper::toDto).toList()

        );
    }

    @Override
    public ApiResponse<List<DisciplineRuleRespDto>> getAllPagination(Pageable pageable) {
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
