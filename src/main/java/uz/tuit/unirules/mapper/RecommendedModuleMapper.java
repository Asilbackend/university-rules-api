package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tuit.unirules.dto.respond_dto.RecommendedModuleRespDto;
import uz.tuit.unirules.entity.recommended_module.RecommendedModule;

@Mapper(componentModel = "spring")
public interface RecommendedModuleMapper {
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "module.id",target = "moduleId")
    RecommendedModuleRespDto toDto(RecommendedModule recommendedModule);
}
