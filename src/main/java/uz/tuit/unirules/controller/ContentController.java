package uz.tuit.unirules.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.dto.respond_dto.ContentRespDto;

import uz.tuit.unirules.services.content.ContentService;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }


    @PostMapping
    public ApiResponse<?> create(@RequestBody ContentCreateDto contentCreateDto) {
        return contentService.create(contentCreateDto);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable Long id) {
        return contentService.get(id);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable Long id, @RequestBody ContentCreateDto contentCreateDto) {
        contentService.update(id, contentCreateDto);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        return contentService.delete(id);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/findAll")
    public ApiResponse<List<?>> getAll(@RequestParam(required = false) Boolean isDeleted) {
        return contentService.getAll(isDeleted);
    }


   /* @GetMapping
    public ApiResponse<List<ContentRespRecordDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return contentService.getAllPagination(pageable);
    }*/



    /*@PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    @GetMapping("/findAllByModuleId")
    public List<ContentRespProjection> getAllByModuleId(@RequestParam Long moduleId) {
        return contentService.getAllByModuleId(moduleId);
    }*/
}
