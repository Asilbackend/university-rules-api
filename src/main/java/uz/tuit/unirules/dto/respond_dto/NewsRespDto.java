package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;

public record NewsRespDto(Long newsId,
                          String name,
                          String description,
                          Long attachmentId) implements Serializable {
}
