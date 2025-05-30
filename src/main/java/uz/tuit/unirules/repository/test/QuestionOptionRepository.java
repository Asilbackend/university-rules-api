package uz.tuit.unirules.repository.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.test.QuestionOption;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption,Long> {
    List<QuestionOption> findAllByIsDeletedFalse();
    Page<QuestionOption> findAllByIsDeletedFalse(Pageable pageable);
}
