package uz.tuit.unirules.repository.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.faculty.group.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findByName(String name);

    @Query(value = """
            SELECT *
            FROM groups g 
            WHERE education_direction_id = :educationDirectionId 
            AND g.is_deleted=false                       
            """, countQuery = """ 
            SELECT COUNT(*) 
            FROM groups g
             WHERE g.is_deleted = false 
             AND g.education_direction_id = :educationDirectionId
            """,nativeQuery = true)
    Page<Group> findGroupsByEducationDirectionId(@Param("educationDirectionId") Long educationDirectionId, Pageable pageable);

    Page<Group> findAllByIsDeletedFalse(Pageable pageable);
}
