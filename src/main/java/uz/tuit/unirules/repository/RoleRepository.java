package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.abs.roles.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String name);

}