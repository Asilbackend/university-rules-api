package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;

public interface DisciplineRuleRepository extends JpaRepository<DisciplineRule,Long> {
}
