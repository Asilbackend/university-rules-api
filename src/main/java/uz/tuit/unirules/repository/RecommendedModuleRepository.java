package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;

public interface RecommendedModuleRepository extends JpaRepository<RecommendedModule,Long> {
}
