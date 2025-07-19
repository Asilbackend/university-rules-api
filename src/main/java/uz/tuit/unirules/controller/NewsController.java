package uz.tuit.unirules.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.NewsRequestDto;
import uz.tuit.unirules.dto.respond_dto.NewsRespDto;
import uz.tuit.unirules.services.news.NewsService;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ApiResponse<NewsRespDto> create(@RequestBody NewsRequestDto dto){
        return newsService.create(dto);
    }
    @GetMapping("/{id}")
    public ApiResponse<NewsRespDto> get(@PathVariable Long id){
        return newsService.get(id);
    }
    @PutMapping("/{id}")
    public ApiResponse<NewsRespDto> update(@PathVariable Long id,@RequestBody NewsRequestDto dto){
        return newsService.update(id,dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleted(@PathVariable Long id){
        return this.newsService.delete(id);
    }
    @GetMapping
    public List<NewsRespDto> getAllNews(){
        return newsService.getAll();
    }
    @GetMapping("/all-page")
    public Page<NewsRespDto> getAllNewsPage(@ParameterObject Pageable pageable){
        return newsService.getAllPagination(pageable);
    }
}
