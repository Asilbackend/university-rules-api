package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.respond_dto.RecommendedModuleRespDto;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;

@Mapper(componentModel = "spring")
public interface RecommendedModuleMapper {
    RecommendedModuleRespDto toDto(RecommendedModule recommendedModule);
}
