package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;

public interface RecommendedModuleRepository extends JpaRepository<RecommendedModule,Long> {
    Page<RecommendedModule> findAllByIsDeletedFalse(Pageable pageable);
}
