package uz.tuit.unirules.controller.faculty;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateEducationDirectionReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateEducationDirectionReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.EducationDirectionRespDto;
import uz.tuit.unirules.services.faculty.EducationDirectionService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/edu-direction")
public class EducationDirectionController implements
        SimpleCrud<Long, CreateEducationDirectionReqDto, UpdateEducationDirectionReqDto, EducationDirectionRespDto> {
    private final EducationDirectionService educationDirectionService;

    public EducationDirectionController(EducationDirectionService educationDirectionService) {
        this.educationDirectionService = educationDirectionService;
    }

    @Override
    @PostMapping
    public ApiResponse<EducationDirectionRespDto> create(@RequestBody CreateEducationDirectionReqDto createEducationReqDto) {
        return educationDirectionService.create(createEducationReqDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> get(@PathVariable(value = "id") Long entityId) {
        return educationDirectionService.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> update(@PathVariable(value = "id") Long entityId,
                                                         @RequestBody UpdateEducationDirectionReqDto updateEducationReqDto) {

        return educationDirectionService.update(entityId, updateEducationReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return educationDirectionService.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<EducationDirectionRespDto>> getAll() {
        return educationDirectionService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<EducationDirectionRespDto>> getAllPagination(Pageable pageable) {
        return educationDirectionService.getAllPagination(pageable);
    }
}
