package uz.tuit.unirules.services.module;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ModuleCreateDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.projections.CertificateProjection;
import uz.tuit.unirules.projections.ModuleUserProjection;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.services.AuthUserService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final AuthUserService authUserService;

    public ModuleService(ModuleRepository moduleRepository, AuthUserService authUserService) {
        this.moduleRepository = moduleRepository;
        this.authUserService = authUserService;
    }


    @Transactional
    public ApiResponse<Module> create(ModuleCreateDto moduleCreateDto) {
        Module build = Module.builder()
                .moduleState(moduleCreateDto.moduleState())
                .name(moduleCreateDto.name())
                .description(moduleCreateDto.description())
                .build();
        moduleRepository.save(build);
        return new ApiResponse<>(201, "saved", true, build);
    }


    public ApiResponse<Module> get(Long entityId) {
        return new ApiResponse<>(200,
                "module", true,
                findById(entityId));
    }

    public Module findById(Long entityId) {
        return moduleRepository.findById(entityId).orElseThrow(() ->
                new EntityNotFoundException("module not found by id = " + entityId));
    }


    @Transactional
    public ApiResponse<Module> update(Long entityId, ModuleCreateDto moduleCreateDto) {
        Module module = findById(entityId);
        module.setDescription(moduleCreateDto.description());
        module.setModuleState(moduleCreateDto.moduleState());
        module.setName(moduleCreateDto.name());
        moduleRepository.save(module);
        return new ApiResponse<>(200, "updated", true, module);
    }


    @Transactional
    public ApiResponse<Module> delete(Long entityId) {
        Module module = findById(entityId);
        module.setIsDeleted(true);
        moduleRepository.save(module);
        return new ApiResponse<>(200, "deleted", true, null);
    }


    @Transactional(readOnly = true)
    public ApiResponse<List<Module>> getAll() {
        List<Module> all = moduleRepository.findAll();
        return new ApiResponse<>(200, "all mosules", true, all);
    }


    public Page<Module> getAllPagination(Pageable pageable) {
        return moduleRepository.findAll(pageable);
    }

    public ApiResponse<Page<Module>> getAllByModuleState(Module.ModuleState moduleState, Pageable pageable) {
        Page<Module> allByModuleState = moduleRepository.findAllByModuleState(moduleState, pageable);
        return new ApiResponse<>(200, "modules", true, allByModuleState);
    }

    @Transactional(readOnly = true)
    public ApiResponse<Module> getModuleVisible(Long id) {
        Module module = findById(id);
        if (module.getModuleState().equals(Module.ModuleState.INVISIBLE)) {
            module = null;
        }
        return new ApiResponse<>(200,
                "module", true,
                module);
    }

    public Page<Module> getAllPaginationVisible(Pageable pageable) {
        return moduleRepository.findAllByModuleStateNot(Module.ModuleState.INVISIBLE, pageable);
    }

    public List<ModuleUserProjection> getModulesByUserWithStatus() {
        Long userId = authUserService.getAuthUserId();
        return moduleRepository.findUserModules(userId);
    }
}
