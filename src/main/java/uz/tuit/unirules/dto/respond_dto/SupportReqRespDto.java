package uz.tuit.unirules.dto.respond_dto;

import uz.tuit.unirules.entity.support_request.SupportRequest;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link uz.tuit.unirules.entity.support_request.SupportRequest}
 */
public record SupportReqRespDto(Long id, Timestamp createdAt, Timestamp updatedAt, Long userId, Long supportUserId,
                                String subject, String message, String responseMessage, Boolean isDeleted,
                                SupportRequest.Status status) implements Serializable {
}