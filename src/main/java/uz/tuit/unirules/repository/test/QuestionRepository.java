package uz.tuit.unirules.repository.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.Test;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    /* @Query(value = """
     SELECT *
     FROM question q
      WHERE Q.is_deleted = FALSE AND q.test.id = :id
             """,nativeQuery = true)
     List<Question> findAllByIsDeletedAndTestId(Long id);*/
    List<Question> findAllByIsDeletedFalse();

    Page<Question> findAllByIsDeletedFalse(Pageable pageable);

    List<Question> findAllByTestIdAndIsDeletedFalseOrderById(Long testId);

    int countByTestIdAndIsDeletedFalse(Long testId);
}
