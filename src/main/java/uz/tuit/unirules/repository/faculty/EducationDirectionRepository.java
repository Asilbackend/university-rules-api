package uz.tuit.unirules.repository.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface EducationDirectionRepository extends JpaRepository<EducationDirection,Long> {
    Optional<EducationDirection> findByName(String name);

    Page<EducationDirection> findAllByIsDeletedFalse(Pageable pageable);
}
