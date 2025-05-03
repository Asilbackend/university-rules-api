package uz.tuit.unirules.controller;

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
public class RecommendedModuleController implements
        SimpleCrud<Long, CreateRecommendedModuleReqDto, UpdateRecommendedModuleReqDto, RecommendedModuleRespDto> {
    private final RecommendedModuleService service;

    public RecommendedModuleController(RecommendedModuleService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ApiResponse<RecommendedModuleRespDto> create(@RequestBody CreateRecommendedModuleReqDto createRecommendedModuleReqDto) {
        return service.create(createRecommendedModuleReqDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> get(@PathVariable(value = "id") Long entityId) {
        return service.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> update(@PathVariable(value = "id") Long entityId,
                                                        @RequestBody UpdateRecommendedModuleReqDto updateRecommendedModuleReqDto) {
        return service.update(entityId, updateRecommendedModuleReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<RecommendedModuleRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return service.delete(entityId);
    }

    @Override
    @GetMapping("all")
    public ApiResponse<List<RecommendedModuleRespDto>> getAll() {
        return service.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<RecommendedModuleRespDto>> getAllPagination(@RequestParam Pageable pageable) {
        return service.getAllPagination(pageable);
    }
}
