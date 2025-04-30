package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.entity.discipline_rule.DisciplineRule;

@Mapper(componentModel = "spring")
public interface DisciplineRuleMapper {
    DisciplineRuleRespDto toDto(DisciplineRule disciplineRule);
}
