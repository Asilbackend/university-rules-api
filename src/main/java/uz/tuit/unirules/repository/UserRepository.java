package uz.tuit.unirules.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            r.role as role
            FROM users u
            JOIN role r ON u.role_id = r.id
        WHERE u.username = ?1
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
            r.role as role
            FROM users u 
            JOIN role r on u.role_id = r.id
            WHERE u.id= ?1
            """, nativeQuery = true
    )
    Optional<UserProjection> findUserById(Long id);

    @Query(value = """ 
            SELECT
            u.id as id,
            u.firstname as firstname,
            u.lastname as lastname,
            u.email as email,
            u.phone as phone,
            u.passed_test as passedTest,
            u.group_id as groupId,
            u.is_deleted as isDeleted       
            FROM users u
            JOIN role r on u.role_id=r.id 
            WHERE u.is_deleted = ?1
            """, nativeQuery = true
    )
    List<UserProjection> findAllUsers(Boolean isDelete);
    Optional<User> findByUsername(String name);

    @Query(value = """
    SELECT  
        u.id,
        u.firstname,
        u.lastname,
        u.email,
        u.phone,
        u.language,
        u.passed_test as passedTest,
        u.group_id as groupId,
        r.role as role
     FROM users u
     JOIN role r ON u.role_id = r.id
     WHERE u.is_deleted = false
       AND u.group_id = :groupId
    """,// pagination ishlatilganda countquery ishlatish majburiy chunki pagination nechta user borligini bilishi shart
            countQuery = """
    SELECT count(*) FROM users u
     WHERE u.is_deleted = false AND u.group_id = :groupId
    """,
            nativeQuery = true) // param buyerda sqldagi groupId ni Param(groupid) ga ulash uchun.
    Page<UserProjection> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);

}
