package uz.tuit.unirules.repository.faculty;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.faculty.group.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findByName(String name);
}
