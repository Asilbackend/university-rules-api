package uz.tuit.unirules.controller.test;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public HttpEntity<?> create(@RequestBody CreateQuestionReqDto dto) {
        return this.questionService.create(dto);
    }

    @PostMapping("/createAll")
    public HttpEntity<?> createAll(@RequestBody List<CreateQuestionReqDto> dtos) {
        for (CreateQuestionReqDto dto : dtos) {
            questionService.create(dto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
