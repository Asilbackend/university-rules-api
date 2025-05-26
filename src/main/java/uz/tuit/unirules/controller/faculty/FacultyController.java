package uz.tuit.unirules.controller.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateFacultyReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateFacultyReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.FacultyRespDto;
import uz.tuit.unirules.services.faculty.FacultyService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ApiResponse<FacultyRespDto> create(@RequestBody CreateFacultyReqDto createFacultyReqDto) {
        return facultyService.create(createFacultyReqDto);

    }

    @GetMapping("/{id}")
    public ApiResponse<FacultyRespDto> get(@PathVariable(value = "id") Long entityId) {
        return facultyService.get(entityId);
    }

    @PutMapping("/{id}")
    public ApiResponse<FacultyRespDto> update(@PathVariable(value = "id")Long entityId,
                                              @RequestBody UpdateFacultyReqDto updateFacultyReqDto) {
        return facultyService.update(entityId,updateFacultyReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<FacultyRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return facultyService.delete(entityId);
    }

    @GetMapping("/all")
    public ApiResponse<List<FacultyRespDto>> getAll() {
        return facultyService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<FacultyRespDto>> getAllPagination(Pageable pageable) {
        return facultyService.getAllPagination(pageable);
    }
}
