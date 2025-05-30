package uz.tuit.unirules.dto.respond_dto;

import java.io.Serializable;
import java.util.List;

public record TestRespDto(Long testId,Long moduleId,String title, String description, List<Long> questionIds
) implements Serializable {
}
