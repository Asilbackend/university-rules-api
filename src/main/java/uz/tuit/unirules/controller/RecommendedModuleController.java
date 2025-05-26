package uz.tuit.unirules.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateRecommendedModuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateRecommendedModuleReqDto;
import uz.tuit.unirules.dto.respond_dto.RecommendedModuleRespDto;
import uz.tuit.unirules.services.recommended_module.RecommendedModuleService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/recommended-module")
public class RecommendedModuleController {
    private final RecommendedModuleService service;

    public RecommendedModuleController(RecommendedModuleService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<RecommendedModuleRespDto> create(@RequestBody CreateRecommendedModuleReqDto createRecommendedModuleReqDto) {
        return service.create(createRecommendedModuleReqDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> update(@PathVariable Long id,
                                                        @RequestBody UpdateRecommendedModuleReqDto updateRecommendedModuleReqDto) {
        return service.update(id, updateRecommendedModuleReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("all")
    public ApiResponse<List<RecommendedModuleRespDto>> getAll() {
        return service.getAll();
    }

    @GetMapping
    public ApiResponse<Page<RecommendedModuleRespDto>> getAllPagination(Pageable pageable) {
        return service.getAllPagination(pageable);
    }
}
