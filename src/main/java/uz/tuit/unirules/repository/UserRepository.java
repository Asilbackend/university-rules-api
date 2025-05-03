package uz.tuit.unirules.repository;

import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.projections.UserProjection;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """ 
            SELECT
            u.id as id,
            u.firstname as firstname,
            u.lastname as lastname,
            u.email as email,
            u.phone as phone,
            u.passed_test as passedTest,
            u.group_id as groupId,
            u.role as role            
            FROM users u 
            WHERE u.id= : id
            """, nativeQuery = true
    )
    Optional<UserProjection> findUserProjectionByUsername(String username);
    @Query(value = """ 
            SELECT
            u.id as id,
            u.firstname as firstname,
            u.lastname as lastname,
            u.email as email,
            u.phone as phone,
            u.passed_test as passedTest,
            u.group_id as groupId,
            u.role as role            
            FROM users u 
            WHERE u.id= : id
            """, nativeQuery = true
    )
    Optional<UserProjection> findUserById(Long entityId);

    @Query(value = """ 
            SELECT
            u.id as id,
            u.firstname as firstname,
            u.lastname as lastname,
            u.email as email,
            u.phone as phone,
            u.passed_test as passedTest,
            u.group_id as groupId,
            u.role as role            
            FROM users u 
            WHERE u.id= : id
            """, nativeQuery = true
    )
    List<UserProjection> findAllUsers(Boolean isDelete);
    Optional<User> findByUsername(String name);

}
