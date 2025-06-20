package uz.tuit.unirules.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    public ApiResponse<Module> create(@RequestBody ModuleCreateDto moduleCreateDto) {
        return moduleService.create(moduleCreateDto);
    }

    @GetMapping("/student/{id}")
    public ApiResponse<Module> get(@PathVariable Long id) {
        //agar student get qilsa module invisible bo'lmasligi kerak
        return moduleService.get(id);
    }

    @PutMapping
    public ApiResponse<Module> update(@RequestParam Long id, @RequestBody ModuleCreateDto moduleCreateDto) {
        return moduleService.update(id, moduleCreateDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Module> delete(@PathVariable Long id) {
        return moduleService.delete(id);
    }


    @GetMapping("/findAll")
    public ApiResponse<List<Module>> getAll() {
        return moduleService.getAll();
    }

    @GetMapping("/student/findAllByModuleState")
    public ApiResponse<Page<Module>> getByVisible(@RequestParam Module.ModuleState moduleState, @ParameterObject Pageable pageable) {
        if (moduleState.equals(Module.ModuleState.INVISIBLE)) {
            throw new RuntimeException("ruxsat etilmagan parametr");
        }
        return moduleService.getAllByModuleState(moduleState, pageable);
    }

    @GetMapping("/findAllByModuleState")
    public ApiResponse<Page<Module>> getAllByModuleState(@RequestParam Module.ModuleState moduleState, @ParameterObject Pageable pageable) {
        return moduleService.getAllByModuleState(moduleState, pageable);
    }


    @GetMapping
    public Page<Module> getAllPagination(
            @ParameterObject Pageable pageable
    ) {
        return moduleService.getAllPagination(pageable);
    }
}
