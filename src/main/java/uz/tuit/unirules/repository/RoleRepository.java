package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.abs.roles.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String name);
    @Query(value = """
            SELECT
             r.id as id,
             r.role as role
             FROM role r WHERE r.id:id;
                        """ ,nativeQuery = true)
    List<Role> getAllRoles();

}