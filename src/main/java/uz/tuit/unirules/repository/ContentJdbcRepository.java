package uz.tuit.unirules.repository;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.controller.student.ContentRespRecordDto;
import java.util.*;

@Repository
public class ContentJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ContentJdbcRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<ContentRespRecordDto> getContentsByModuleIdForPage(Long moduleId, Long userId) {
        String sql = """
                 SELECT c.id                               AS contentId,
                        c.title                            as contentTitle,
                        coalesce(cs.status, 'NOT_STARTED') as userContentStatus,
                        c.is_required or cs.is_required    as isRequiredContent,
                        json_agg(
                                json_build_object(
                                        'attachmentId', a.id,
                                        'title', ce.title,
                                        'contentType', CASE
                                                           WHEN ce.text is not null THEN 'text'
                                                           WHEN a.attach_type is null then 'UNKNOWN'
                                                           ELSE a.attach_type
                                            END,
                                        'videoDuration', a.video_duration,
                                        'isRead', coalesce(ats.is_read or ces.is_read, false),
                                        'order_element', ce.order_element
                                )
                        )                                  AS attachmentDetails
                 FROM content_element ce
                          LEFT JOIN content c ON ce.content_id = c.id
                          LEFT JOIN attachment a ON ce.attachment_id = a.id
                          LEFT JOIN content_element_student ces ON ces.content_element_id = ce.id
                     and ces.student_id = :userId
                          LEFT JOIN attachment_student ats on a.id = ats.attachment_id
                     and ats.student_id = :userId
                          LEFT JOIN content_student cs on cs.content_id = c.id
                     AND cs.user_id = :userId
                     AND cs.is_deleted = false
                 WHERE c.module_id = :moduleId
                   and c.is_deleted = false
                 group by c.id, cs.status, cs.is_required
                 order by c.id, min(ce.order_element);
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("moduleId", moduleId);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            ContentRespRecordDto dto = new ContentRespRecordDto();
            dto.setContentId(rs.getLong("contentId"));
            dto.setContentTitle(rs.getString("contentTitle"));
            dto.setUserContentStatus(rs.getString("userContentStatus"));
            dto.setIsRequiredContent(rs.getBoolean("isRequiredContent"));

            String attachmentDetailsJson = rs.getString("attachmentDetails");
            List<Map<Object, Object>> attachmentDetailsMap;
            try {
                attachmentDetailsMap = objectMapper.readValue(
                        attachmentDetailsJson,
                        new TypeReference<>() {
                        }
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            dto.setAttachmentDetails(attachmentDetailsMap);
            return dto;
        });
    }
}
