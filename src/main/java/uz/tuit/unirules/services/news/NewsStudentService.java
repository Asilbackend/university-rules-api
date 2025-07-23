package uz.tuit.unirules.services.news;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.entity.news.News;
import uz.tuit.unirules.entity.news.NewsStudent;
import uz.tuit.unirules.entity.news.NewsStudentRepository;
import uz.tuit.unirules.repository.NewsRepository;
import uz.tuit.unirules.services.user.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsStudentService {
    private final NewsStudentRepository newsStudentRepository;
    private final UserService userService;
    private final NewsRepository newsRepository;

    public NewsStudent findIfCreate(Long newsId, Long studentId) {
        Optional<NewsStudent> optionalNewsStudent = newsStudentRepository.findByNewsIdAndStudentId(newsId, studentId);
        return optionalNewsStudent.orElseGet(() -> newsStudentRepository.save(
                NewsStudent.builder()
                        .student(userService.findByUserId(studentId))
                        .isLike(false)
                        .isSeen(false)
                        .news(newsRepository.findById(newsId).orElseThrow())
                        .build()));
    }
}
