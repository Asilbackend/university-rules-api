package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.content_student.ContentStudent;

public interface ContentStudentRepository extends JpaRepository<ContentStudent, Long> {
}
