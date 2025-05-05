package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import uz.tuit.unirules.dto.RoleDto;
import uz.tuit.unirules.entity.abs.roles.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
}
