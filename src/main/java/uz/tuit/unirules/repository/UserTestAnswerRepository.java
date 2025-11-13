package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.user_test.UserTestAnswer;

import java.util.List;
import java.util.Optional;

public interface UserTestAnswerRepository extends JpaRepository<UserTestAnswer, Long> {
    List<UserTestAnswer> findAllByUserTestIdAndIsDeletedFalse(Long userTestId);

    @Query("select uta from UserTestAnswer uta where uta.questionOption.question.id=:questionId and uta.userTest.id=:userTestId")
    Optional<UserTestAnswer> findByUserTestIdAndQuestionId(Long userTestId, Long questionId);

    long countByUserTestIdAndQuestionOption_IsCorrectTrueAndIsDeletedFalse(Long id);
}