package uz.tuit.unirules.services.module;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.ModuleCreateDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.repository.ModuleRepository;

import java.util.List;

@Service
public class ModuleService implements SimpleCrud<Long, ModuleCreateDto, ModuleCreateDto, Module> {
    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
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

    @Override
    public ApiResponse<Module> get(Long entityId) {
        return new ApiResponse<>(200,
                "module", true,
                findById(entityId));
    }

    public Module findById(Long entityId) {
        return moduleRepository.findById(entityId).orElseThrow(() ->
                new EntityNotFoundException("module not found by id = " + entityId));
    }

    @Override
    @Transactional
    public ApiResponse<Module> update(Long entityId, ModuleCreateDto moduleCreateDto) {
        Module module = findById(entityId);
        module.setDescription(moduleCreateDto.description());
        module.setModuleState(moduleCreateDto.moduleState());
        module.setName(moduleCreateDto.name());
        moduleRepository.save(module);
        return new ApiResponse<>(200, "updated", true, module);
    }

    @Override
    @Transactional
    public ApiResponse<Module> delete(Long entityId) {
        Module module = findById(entityId);
        module.setIsDeleted(true);
        moduleRepository.save(module);
        return new ApiResponse<>(200, "deleted", true, null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<Module>> getAll() {
        List<Module> all = moduleRepository.findAll();
        return new ApiResponse<>(200, "all mosules", true, all);
    }

    @Override
    public ApiResponse<List<Module>> getAllPagination(Pageable pageable) {
        Page<Module> all = moduleRepository.findAll(pageable);
        List<Module> content = all.getContent();
        return new ApiResponse<>(200, "modules page", true, content);
    }
}
