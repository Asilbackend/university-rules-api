package uz.tuit.unirules.controller.student;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.projections.AttachmentNewsProjection;
import uz.tuit.unirules.projections.NewsVideoProjection;
import uz.tuit.unirules.services.news.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/student/news")
public class NewsPageController {
    private final NewsService newsService;

    public NewsPageController(NewsService newsService) {
        this.newsService = newsService;
    }

   /* @GetMapping("/get/news")
    public Page<AttachmentNewsProjection> getAttachmentByThumbNail(Pageable pageable) {
        return newsService.findAttachmentNewsPro(pageable);
    }*/

    @GetMapping("/getNext")
    public List<AttachmentNewsProjection> getNextNews(@RequestParam(required = false) Long lastContentId, @RequestParam Integer size) {
        return newsService.getNextNews(lastContentId, size);
    }

    @GetMapping("/{newsId}")
    public HttpEntity<NewsVideoProjection> getNewsVideoPage(@PathVariable Long newsId) {
        return ResponseEntity.ok(newsService.getNews(newsId));
    }

    @PostMapping("/like")// like bosish yoki qaytarib olish
    public HttpEntity<?> likeBoss(
            @RequestParam Long newsId
    ) {
        newsService.like(newsId);
        return ResponseEntity.noContent().build();
    }
}
