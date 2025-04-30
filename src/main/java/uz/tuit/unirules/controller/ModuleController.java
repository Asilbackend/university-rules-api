package uz.tuit.unirules.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.ModuleCreateDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.List;

@RestController
@RequestMapping("/api/module")
public class ModuleController implements SimpleCrud<Long, ModuleCreateDto, ModuleCreateDto, Module> {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Override
    @PostMapping
    public ApiResponse<Module> create(@RequestBody ModuleCreateDto moduleCreateDto) {
        return moduleService.create(moduleCreateDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<Module> get(@PathVariable Long id) {
        return moduleService.get(id);
    }

    @Override
    @PutMapping
    public ApiResponse<Module> update(@RequestParam Long id, @RequestBody ModuleCreateDto moduleCreateDto) {
        return moduleService.update(id, moduleCreateDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<Module> delete(@PathVariable Long id) {
        return moduleService.delete(id);
    }

    @Override
    @GetMapping("/findAll")
    public ApiResponse<List<Module>> getAll() {
        return moduleService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<Module>> getAllPagination(@ParameterObject Pageable pageable) {
        return moduleService.getAllPagination(pageable);
    }
}
