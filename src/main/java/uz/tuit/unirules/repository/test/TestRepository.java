package uz.tuit.unirules.repository.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.test.Test;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllByIsDeletedFalse();

    Page<Test> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Test> findByModuleId(Long moduleId);
}
