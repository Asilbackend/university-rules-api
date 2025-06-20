package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.modul.Module;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Query("select m from Module m  where m.moduleState=:moduleState and m.isDeleted=false")
    Page<Module> findAllByModuleState(Module.ModuleState moduleState, Pageable pageable);

    Page<Module> findAllByModuleStateNot(Module.ModuleState moduleState, Pageable pageable);


}
