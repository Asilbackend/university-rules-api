package uz.tuit.unirules.repository.user_test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.user_test.UserTest;
@Repository
public interface UserTestRepository extends JpaRepository<UserTest,Long> {
}
