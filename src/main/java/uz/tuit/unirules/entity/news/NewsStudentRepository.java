package uz.tuit.unirules.entity.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NewsStudentRepository extends JpaRepository<NewsStudent, Long> {
    @Query("select n from NewsStudent  n where n.student.id=:userId and n.id=:newsId and n.news.isDeleted=false")
    Optional<NewsStudent> findByNewsIdAndStudentId(Long newsId, Long userId);
}