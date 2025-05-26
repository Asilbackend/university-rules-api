package uz.tuit.unirules.controller.faculty;

import org.springframework.data.domain.Page;
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
public class EducationDirectionController {
    private final EducationDirectionService educationDirectionService;

    public EducationDirectionController(EducationDirectionService educationDirectionService) {
        this.educationDirectionService = educationDirectionService;
    }

    @PostMapping
    public ApiResponse<EducationDirectionRespDto> create(@RequestBody CreateEducationDirectionReqDto createEducationReqDto) {
        return educationDirectionService.create(createEducationReqDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> get(@PathVariable(value = "id") Long entityId) {
        return educationDirectionService.get(entityId);
    }

    @PutMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> update(@PathVariable(value = "id") Long entityId,
                                                         @RequestBody UpdateEducationDirectionReqDto updateEducationReqDto) {

        return educationDirectionService.update(entityId, updateEducationReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<EducationDirectionRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return educationDirectionService.delete(entityId);
    }

    @GetMapping("/all")
    public ApiResponse<List<EducationDirectionRespDto>> getAll() {
        return educationDirectionService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<EducationDirectionRespDto>> getAllPagination(Pageable pageable) {
        return educationDirectionService.getAllPagination(pageable);
    }
}
