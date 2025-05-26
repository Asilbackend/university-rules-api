package uz.tuit.unirules.services.recommended_module;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateRecommendedModuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateRecommendedModuleReqDto;
import uz.tuit.unirules.dto.respond_dto.RecommendedModuleRespDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.mapper.RecommendedModuleMapper;
import uz.tuit.unirules.repository.RecommendedModuleRepository;
import uz.tuit.unirules.services.module.ModuleService;
import uz.tuit.unirules.services.user.UserService;

import java.util.List;

@Service
public class RecommendedModuleService  {
    private final RecommendedModuleRepository recommendedModuleRepository;
    private final RecommendedModuleMapper recommendedModuleMapper;
    private final UserService userService;
    private final ModuleService moduleService;

    public RecommendedModuleService(RecommendedModuleRepository recommendedModuleRepository,
                                    RecommendedModuleMapper recommendedModuleMapper, UserService userService, ModuleService moduleService) {
        this.recommendedModuleRepository = recommendedModuleRepository;
        this.recommendedModuleMapper = recommendedModuleMapper;
        this.userService = userService;
        this.moduleService = moduleService;
    }

    @Transactional
    public ApiResponse<RecommendedModuleRespDto> create(CreateRecommendedModuleReqDto createRecommendedModuleReqDto) {
        //module ni olib keladi
        Module module = moduleService.findById(createRecommendedModuleReqDto.moduleId());
        // userni olib keladi
        User user = userService.findByUserId(createRecommendedModuleReqDto.userId());
        RecommendedModule recommendedModule = RecommendedModule.builder()
                .reason(createRecommendedModuleReqDto.reason())
                .module(module)
                .user(user)
                .build();
        recommendedModuleRepository.save(recommendedModule);
        return new ApiResponse<>(
                201,
                "Recommended Module is saved",
                true,
                recommendedModuleMapper.toDto(recommendedModule)
        );
    }

    public ApiResponse<RecommendedModuleRespDto> get(Long entityId) {
        RecommendedModule recommendedModule = findById(entityId);
        return new ApiResponse<>(
                200,
                "recommended module is found successfully",
                true,
                recommendedModuleMapper.toDto(recommendedModule)
        );
    }

    public RecommendedModule findById(Long entityId) {
        return recommendedModuleRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(" recommended module does not exist"));
    }

    @Transactional
    public ApiResponse<RecommendedModuleRespDto> update(Long entityId, UpdateRecommendedModuleReqDto updateRecommendedModuleReqDto) {
        RecommendedModule recommendedModule = findById(entityId);
        recommendedModule.setReason(updateRecommendedModuleReqDto.reason());
        recommendedModuleRepository.save(recommendedModule);
        return new ApiResponse<>(
                200,
                "recommendedModule is updated",
                true,
                recommendedModuleMapper.toDto(recommendedModule)
        );
    }

    @Transactional
    public ApiResponse<RecommendedModuleRespDto> delete(Long entityId) {
        RecommendedModule recommendedModule = findById(entityId);
        recommendedModuleRepository.delete(recommendedModule);
        return new ApiResponse<>(
                200,
                "recommended module is deleted",
                true,
                recommendedModuleMapper.toDto(recommendedModule)
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<RecommendedModuleRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all recommended module as list",
                true,
                recommendedModuleRepository.findAll().stream().map
                        (recommendedModuleMapper::toDto).toList()

        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<RecommendedModuleRespDto>> getAllPagination(Pageable pageable) {
        Page<RecommendedModule> recommendedModulePage = recommendedModuleRepository.findAllByIsDeletedFalse(pageable);
        return new ApiResponse<>(
                200,
                "all recommended modules as pages",
                true,
                recommendedModulePage.map(recommendedModuleMapper::toDto)

        );
    }

    public Page<RecommendedModule> findAllPage(Pageable pageable) {
        return recommendedModuleRepository.findAll(pageable);
    }
}
