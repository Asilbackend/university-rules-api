package uz.tuit.unirules.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;
import uz.tuit.unirules.services.content.ContentService;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController implements SimpleCrud<Long, ContentCreateDto, ContentCreateDto, ContentRespDto> {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @Override
    @PostMapping
    public ApiResponse<ContentRespDto> create(ContentCreateDto contentCreateDto) {
        return contentService.create(contentCreateDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<ContentRespDto> get(@PathVariable Long id) {
        return contentService.get(id);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<ContentRespDto> update(@PathVariable Long id, @RequestBody ContentCreateDto contentCreateDto) {
        return contentService.update(id, contentCreateDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<ContentRespDto> delete(@PathVariable Long id) {
        return contentService.delete(id);
    }

    @Override
    @GetMapping("/findAll")
    public ApiResponse<List<ContentRespDto>> getAll() {
        return contentService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<ContentRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return contentService.getAllPagination(pageable);
    }
}
