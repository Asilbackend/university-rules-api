package uz.tuit.unirules.repository.discipline_rule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;

@Repository
public interface DisciplineRuleRepository extends JpaRepository<DisciplineRule,Long> {
}
