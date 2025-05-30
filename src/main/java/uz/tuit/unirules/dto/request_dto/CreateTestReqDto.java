package uz.tuit.unirules.dto.request_dto;

import java.io.Serializable;
import java.util.List;

public record CreateTestReqDto(
      Long moduleId, String title, String description
) implements Serializable {
}
