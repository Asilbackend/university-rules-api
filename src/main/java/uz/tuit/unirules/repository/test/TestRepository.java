package uz.tuit.unirules.repository.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.test.Test;

@Repository
public interface TestRepository extends JpaRepository<Test,Long> {
}
