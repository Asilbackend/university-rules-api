package uz.tuit.unirules.controller.test;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateTestReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateTestReqDto;
import uz.tuit.unirules.dto.respond_dto.TestRespDto;
import uz.tuit.unirules.services.test.TestService;

import java.util.List;
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping
    public ApiResponse<TestRespDto> create( @RequestBody CreateTestReqDto createReqDto) {
        return testService.create(createReqDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<TestRespDto> get(@PathVariable Long id) {
        return testService.get(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<TestRespDto> update(@PathVariable Long id,@RequestBody UpdateTestReqDto updateReqDto) {
        return testService.update(id,updateReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<TestRespDto> delete(@PathVariable Long id) {
        return testService.delete(id);
    }

    @GetMapping("/all")
    public ApiResponse<List<TestRespDto>> getAll() {
        return testService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<TestRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return testService.getAllPagination(pageable);
    }
}
