package uz.tuit.unirules.controller.test;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateQuestionReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateQuestionReqDto;
import uz.tuit.unirules.dto.respond_dto.QuestionRespDto;
import uz.tuit.unirules.services.test.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ApiResponse<QuestionRespDto> create(@RequestBody CreateQuestionReqDto dto) {
        return this.questionService.create(dto);
    }

    @GetMapping("/{id}")
    public ApiResponse<QuestionRespDto> get(@PathVariable Long id) {
        return this.questionService.get(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<QuestionRespDto> update(@PathVariable Long id, @RequestBody UpdateQuestionReqDto dto) {
        return this.questionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<QuestionRespDto> delete(@PathVariable Long id) {
        return this.questionService.delete(id);
    }

    @GetMapping("/all")
    public ApiResponse<List<QuestionRespDto>> getAll() {
        return this.questionService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<QuestionRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return this.questionService.getAllPagination(pageable);
    }
}
