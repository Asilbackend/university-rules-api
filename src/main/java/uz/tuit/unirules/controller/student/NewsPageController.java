package uz.tuit.unirules.controller.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tuit.unirules.projections.AttachmentNewsProjection;
import uz.tuit.unirules.projections.AttachmentUrlProjection;
import uz.tuit.unirules.services.news.NewsService;

@RestController
@RequestMapping("/api/student/news")
public class NewsPageController {
    private final NewsService newsService;
    public NewsPageController(NewsService newsService) {
        this.newsService = newsService;
    }
    @GetMapping("/get/news/{newsId}/video")
    public AttachmentUrlProjection getUrlByNewsId(@PathVariable Long newsId){
        return newsService.findUrlByNewsId(newsId);
    }
    @GetMapping("/get/news")
    public Page<AttachmentNewsProjection> getAttachmentByThumbNail(Pageable pageable){
        return newsService.findAttachmentNewsPro(pageable);
    }
}
