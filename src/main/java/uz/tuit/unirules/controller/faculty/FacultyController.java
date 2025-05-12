package uz.tuit.unirules.controller.faculty;

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
public class FacultyController implements
        SimpleCrud<Long, CreateFacultyReqDto, UpdateFacultyReqDto, FacultyRespDto> {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    @PostMapping
    public ApiResponse<FacultyRespDto> create(@RequestBody CreateFacultyReqDto createFacultyReqDto) {
        return facultyService.create(createFacultyReqDto);

    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<FacultyRespDto> get(@PathVariable(value = "id") Long entityId) {
        return facultyService.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<FacultyRespDto> update(@PathVariable(value = "id")Long entityId,
                                              @RequestBody UpdateFacultyReqDto updateFacultyReqDto) {
        return facultyService.update(entityId,updateFacultyReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<FacultyRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return facultyService.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<FacultyRespDto>> getAll() {
        return facultyService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<FacultyRespDto>> getAllPagination( Pageable pageable) {
        return facultyService.getAllPagination(pageable);
    }
}
