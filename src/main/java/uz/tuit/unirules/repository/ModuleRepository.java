package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.modul.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {

}
