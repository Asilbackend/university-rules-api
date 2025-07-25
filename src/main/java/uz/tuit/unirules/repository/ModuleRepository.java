package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.controller.student.ProfileController;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.projections.ModuleUserProjection;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Query("select m from Module m  where m.moduleState=:moduleState and m.isDeleted=false")
    Page<Module> findAllByModuleState(Module.ModuleState moduleState, Pageable pageable);

    Page<Module> findAllByModuleStateNot(Module.ModuleState moduleState, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Content c WHERE c.id = :contentId AND c.module.moduleState = 'REQUIRED'")
    boolean isRequiredContentByContentId(Long contentId);

    @Query(value = """
            select m.id                        as moduleId,
                   m.name                      as moduleName,
                   m.description               as moduleDescription,
                   coalesce(max(ut.result), 0) as moduleTestResult,
                   case
                       when bool_or(ut.success is true) then 'COMPLETED'
                       when bool_or(ut.success is false) then 'FAILED'
                       else 'IN_PROGRESS'
                       end                     as userModuleStatus
            from content_student cs
                     left join content c on cs.content_id = c.id
                     left join module m on m.id = c.module_id
                     left join test t on m.id = t.module_id
                     left join user_test ut on t.id = ut.test_id and ut.user_id = cs.user_id
            where cs.status <> :status
              and cs.user_id = :userId
              and cs.is_deleted = false
              and c.is_deleted = false
              and m.is_deleted = false
            group by m.id, m.name, m.description
            order by count(cs.started_at)
            """, nativeQuery = true)
    List<ModuleUserProjection> findUserModules(Long userId);


    @Query(value = """
            SELECT m.id                        AS moduleId,
                   m.name                      AS moduleName,
                   m.description               AS moduleDescription,
                   coalesce(max(ut.result), 0) AS moduleTestResult,
                   CASE
                       WHEN bool_or(ut.success IS TRUE) THEN 'COMPLETED'
                       WHEN bool_or(ut.success IS FALSE) THEN 'FAILED'
                       ELSE 'IN_PROGRESS'
                       END                     AS userModuleStatus
            FROM content_student AS cs
                     LEFT JOIN content   AS c ON  c.id = cs.content_id
                     LEFT JOIN module    AS m ON  m.id = c.module_id
                     LEFT JOIN test      AS t ON  m.id = t.module_id
                     LEFT JOIN user_test AS ut ON t.id = ut.test_id AND ut.user_id = cs.user_id
            WHERE cs.status      = :status
              AND cs.user_id     = :userId
              AND cs.is_deleted  = FALSE
              AND  c.is_deleted  = FALSE
              AND  m.is_deleted  = FALSE
            GROUP BY m.id, m.name, m.description
            ORDER BY COUNT(cs.started_at);
                        
            """, nativeQuery = true)
    List<ModuleUserProjection> findUserModules(@Param(value = "status") ProfileController.UserModuleStatus status,
                                               @Param(value = "userId") Long userId
    );

}
