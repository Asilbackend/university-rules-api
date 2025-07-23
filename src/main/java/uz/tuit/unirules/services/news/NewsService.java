package uz.tuit.unirules.services.news;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.projections.NewsVideoProjection;
import uz.tuit.unirules.dto.ApiResponse;

import uz.tuit.unirules.dto.request_dto.NewsRequestDto;
import uz.tuit.unirules.dto.respond_dto.NewsRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.news.News;
import uz.tuit.unirules.entity.news.NewsStudent;
import uz.tuit.unirules.entity.news.NewsStudentRepository;
import uz.tuit.unirules.mapper.NewsMapper;
import uz.tuit.unirules.projections.AttachmentNewsProjection;
import uz.tuit.unirules.projections.AttachmentUrlProjection;
import uz.tuit.unirules.repository.NewsRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final AttachmentService attachmentService;
    private final AuthUserService authUserService;
    private final NewsStudentRepository newsStudentRepository;
    private final NewsStudentService newsStudentService;

    @Transactional
    public ApiResponse<NewsRespDto> create(NewsRequestDto dto) {
        Attachment attachment = attachmentService.findById(dto.attachmentId());
        News news = News.builder()
                .name(dto.name())
                .description(dto.description())
                .attachment(attachment)
                .build();
        newsRepository.save(news);
        return new ApiResponse<>(
                201,
                "saved",
                true,
                newsMapper.toRespDto(news)
        );
    }

    public ApiResponse<NewsRespDto> get(Long id) {
        return new ApiResponse<>(
                200,
                "found",
                true,
                newsMapper.toRespDto(findNewsById(id))
        );
    }

    public News findNewsById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("news are not found by this id = %s".formatted(id)));
    }

    @Transactional
    public ApiResponse<NewsRespDto> update(Long id, NewsRequestDto dto) {
        News news = findNewsById(id);
        Attachment attachment = attachmentService.findById(dto.attachmentId());
        news.setName(dto.name());
        news.setDescription(dto.description());
        news.setAttachment(attachment);
        newsRepository.save(news);
        return new ApiResponse<>(
                200,
                "updated",
                true,
                newsMapper.toRespDto(news));
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        News news = findNewsById(id);
        news.setIsDeleted(true);
        newsRepository.save(news);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<NewsRespDto> getAll() {
        return newsRepository
                .findAllByIsDeletedFalse().stream().map
                        (newsMapper::toRespDto).toList();

    }

    @Transactional(readOnly = true)
    public Page<NewsRespDto> getAllPagination(Pageable pageable) {
        return newsRepository.findAllByIsDeletedFalse(pageable).map(newsMapper::toRespDto);
    }

    @Transactional
    public Page<AttachmentNewsProjection> findAttachmentNewsPro(Pageable pageable) {
        return newsRepository.findAttachmentNews(pageable);
    }

    public AttachmentUrlProjection findUrlByNewsId(Long newsId) {
        return newsRepository.findAttachmentUrl(newsId);
    }


    public List<AttachmentNewsProjection> getNextNews(LocalDateTime lastCreatedAt, int size) {
        Long authUserId = authUserService.getAuthUserId();
        if (lastCreatedAt == null) {
            return newsRepository.findNextStoriesNative(LocalDateTime.MIN, size, true, authUserId);
        } else {
            return newsRepository.findNextStoriesNative(lastCreatedAt, size, false, authUserId);
        }
    }

    public NewsVideoProjection getNews(Long newsId) {
        NewsStudent newsStudent = newsStudentService.findIfCreate(newsId, authUserService.getAuthUserId());
        if (!newsStudent.getIsSeen()) {
            newsStudent.setIsSeen(true);
            newsStudentRepository.save(newsStudent);
        }
        return newsRepository.findNewsVideoInfo(newsId, authUserService.getAuthUserId()).orElseThrow();
    }

    public void like(Long newsId) {
        NewsStudent newsStudent = newsStudentService.findIfCreate(newsId, authUserService.getAuthUserId());
        newsStudent.setIsLike(!newsStudent.getIsLike());
        newsStudentRepository.save(newsStudent);
    }
}
