package uz.tuit.unirules.controller.test;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateQuestionOptionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionOptionReqDto;
import uz.tuit.unirules.dto.respond_dto.QuestionOptionRespDto;
import uz.tuit.unirules.services.test.QuestionOptionService;

import java.util.List;

@RestController
@RequestMapping("/api/question-option")
public class QuestionOptionController {
    private final QuestionOptionService service;

    public QuestionOptionController(QuestionOptionService service) {
        this.service = service;
    }
    @PostMapping
    public ApiResponse<QuestionOptionRespDto> create(@RequestBody CreateQuestionOptionReqDto createDto){
        return service.create(createDto);
    }
    @GetMapping("/{id}")
    public ApiResponse<QuestionOptionRespDto> get(@PathVariable Long id) {
        return service.get(id);
    }
    @PutMapping("/{id}")
    public ApiResponse<QuestionOptionRespDto> update(@PathVariable Long id, @RequestBody UpdateQuestionOptionReqDto updateDto) {
        return service.update(id,updateDto);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<QuestionOptionRespDto> delete(@PathVariable Long id) {
        return service.delete(id);
    }
    @GetMapping("/all")
    public ApiResponse<List<QuestionOptionRespDto>> getAll() {
        return service.getAll();
    }
    @GetMapping
    public ApiResponse<Page<QuestionOptionRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return service.getAllPagination(pageable);
    }
}
