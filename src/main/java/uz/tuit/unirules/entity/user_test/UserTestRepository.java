package uz.tuit.unirules.entity.user_test;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTestRepository extends JpaRepository<UserTest, Long> {
    Optional<UserTest> findByUserIdAndTestId(Long userId, Long testId);
}