package uz.tuit.unirules.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.abs.roles.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}