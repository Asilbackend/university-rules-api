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

    @Query("select m from Module m  where m.moduleState=:moduleState and m.isDeleted=false order by m.createdAt desc")
    Page<Module> findAllByModuleState(Module.ModuleState moduleState, Pageable pageable);

    Page<Module> findAllByModuleStateNot(Module.ModuleState moduleState, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Content c WHERE c.id = :contentId AND c.module.moduleState = 'REQUIRED'")
    boolean isRequiredContentByContentId(Long contentId);

    @Query(value = """
            SELECT m.id                                 AS module_id,
                   m.name                               AS module_name,
                   m.description                        AS module_description,
                   m.module_state                       as module_state,
                   COALESCE(mt.module_test_result, 0)   AS module_test_result,
                   COALESCE(case
                                when ce_data.read_percentage >= 95 then 'COMPLETED'
                                when mt.failed then 'FAILED'
                                when ce_data.read_elements > 0 then 'IN_PROGRESS'
                                when ce_data.read_elements = 0 then 'NOT_STARTED'
                                end, '')                AS user_module_status,
                   COALESCE(ce_data.read_percentage, 0) AS read_percentage
            FROM module m
                     LEFT JOIN LATERAL (
                SELECT COUNT(ce.id)                                    AS total_elements,
                       COUNT(CASE WHEN ces.is_read IS TRUE THEN 1 END) AS read_elements,
                       ROUND(
                               (COUNT(CASE WHEN ces.is_read IS TRUE THEN 1 END)::decimal /
                                NULLIF(COUNT(ce.id), 0)) * 100, 2
                       )                                               AS read_percentage
                FROM content c
                         JOIN content_element ce ON ce.content_id = c.id
                         LEFT JOIN content_element_student ces
                                   ON ces.content_element_id = ce.id
                                       AND ces.student_id = :userId
                WHERE c.module_id = m.id
                  AND c.is_deleted = FALSE
                ) ce_data ON TRUE
            
                     LEFT JOIN LATERAL (
                SELECT COALESCE(MAX(ut.result), 0)  AS module_test_result,
                       BOOL_OR(ut.success IS FALSE) as failed
                FROM test t
                         LEFT JOIN user_test ut ON ut.test_id = t.id AND ut.user_id = :userId
                WHERE t.module_id = m.id
                ) mt ON TRUE
            WHERE m.is_deleted = FALSE
              and m.module_state <> 'INVISIBLE'
            ORDER BY m.id desc;
            """, nativeQuery = true)
    List<ModuleUserProjection> findUserModules(Long userId);

   /* @Query(value = """
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
    );*/

}
