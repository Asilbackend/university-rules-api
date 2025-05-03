package uz.tuit.unirules.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.services.discipline_rule.DisciplineRuleService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/discipline-rule")
public class DisciplineRuleController implements
        SimpleCrud<Long, CreateDisciplineRuleReqDto, UpdateDisciplineRuleReqDto, DisciplineRuleRespDto> {
private final DisciplineRuleService disciplineRuleService;

    public DisciplineRuleController(DisciplineRuleService disciplineRuleService) {
        this.disciplineRuleService = disciplineRuleService;
    }

    @Override
    @PostMapping
    public ApiResponse<DisciplineRuleRespDto> create(@RequestBody CreateDisciplineRuleReqDto createDisciplineRuleReqDto) {
        return disciplineRuleService.create(createDisciplineRuleReqDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<DisciplineRuleRespDto> get(@PathVariable(value = "id") Long entityId) {
        return disciplineRuleService.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<DisciplineRuleRespDto> update(@PathVariable(value = "id") Long entityId,
                                                     @RequestBody UpdateDisciplineRuleReqDto updateDisciplineRuleReqDto) {
        return disciplineRuleService.update(entityId,updateDisciplineRuleReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<DisciplineRuleRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return disciplineRuleService.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<DisciplineRuleRespDto>> getAll() {
        return disciplineRuleService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<DisciplineRuleRespDto>> getAllPagination(Pageable pageable) {
        return disciplineRuleService.getAllPagination(pageable);
    }
}
