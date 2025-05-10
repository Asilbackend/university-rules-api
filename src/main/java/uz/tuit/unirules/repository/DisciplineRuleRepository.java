package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;
import uz.tuit.unirules.projections.DisciplineRuleProjection;

import java.util.List;
import java.util.Optional;

public interface DisciplineRuleRepository extends JpaRepository<DisciplineRule,Long> {
    @Query(value = """ 
            SELECT dr.id as id,
            dr.attachment_id as attachmentId,
            dr.title as title
            FROM discipline_rule dr
             WHERE dr.is_deleted =: isDeleted;
            """,nativeQuery = true)
    List<DisciplineRuleProjection> findAllDisciplineRules(Boolean isDeleted);

    @Query(value = """ 
            SELECT dr.id as id,
            dr.attachment_id as attachmentId,
            dr.title as title
            FROM discipline_rule dr
             WHERE dr.id =: id;
            """,nativeQuery = true)
    Optional<DisciplineRuleProjection> findDisciplineRuleById(Long id);
}
