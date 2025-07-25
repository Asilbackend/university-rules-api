package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;
import uz.tuit.unirules.projections.DisciplineRuleProjection;

import java.util.List;
import java.util.Optional;

public interface DisciplineRuleRepository extends JpaRepository<DisciplineRule, Long> {
    @Query(value = """ 
            SELECT dr.id as id,
            dr.attachment_id as attachmentId,
            dr.title as title,
            dr.body as body
            FROM discipline_rule dr
             WHERE dr.is_deleted = ?1;
            """, nativeQuery = true)
    List<DisciplineRuleProjection> findAllDisciplineRules(Boolean isDeleted);

    @Query(value = """ 
            SELECT dr.id as id,
            dr.attachment_id as attachmentId,
            dr.title as title,
            dr.body as body
            FROM discipline_rule dr
             WHERE dr.is_deleted = false AND dr.id = ?1;
            """, nativeQuery = true)
    Optional<DisciplineRuleProjection> findDisciplineRuleById(Long id);

    @Query(value = """ 
            SELECT dr.id as id,
            dr.attachment_id as attachmentId,
            dr.title as title,
            dr.body as body
            FROM discipline_rule dr
             WHERE is_deleted = FALSE;
            """, countQuery = "SELECT COUNT() FROM discipline_rule  WHERE is_deleted = FALSE",
            nativeQuery = true)
    Page<DisciplineRuleProjection> findDisciplineRulePages(Pageable pageable);

    @Query("""
            SELECT dr
            FROM DisciplineRule dr
            WHERE (:isDeleted IS NULL OR dr.isDeleted = :isDeleted)
            """)
    Page<DisciplineRule> findAllByIsDeleted(@Param("isDeleted") Boolean isDeleted, Pageable pageable);


}
