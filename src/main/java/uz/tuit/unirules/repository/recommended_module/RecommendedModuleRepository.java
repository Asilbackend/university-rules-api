package uz.tuit.unirules.repository.recommended_module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;

@Repository
public interface RecommendedModuleRepository extends JpaRepository<RecommendedModule,Long> {
}
