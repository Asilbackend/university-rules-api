package uz.tuit.unirules.repository.faculty;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.faculty.Faculty;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    Optional<Faculty> findByName(String name);
}
