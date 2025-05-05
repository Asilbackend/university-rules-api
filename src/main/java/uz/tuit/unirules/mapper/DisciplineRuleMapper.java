package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;

@Mapper(componentModel = "spring")
public interface DisciplineRuleMapper {
    @Mapping(source = "attachment.id",target = "attachmentId")
    DisciplineRuleRespDto toDto(DisciplineRule disciplineRule);
}
