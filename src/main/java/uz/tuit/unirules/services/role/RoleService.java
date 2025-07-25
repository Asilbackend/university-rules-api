package uz.tuit.unirules.services.role;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.RoleDto;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.mapper.RoleMapper;
import uz.tuit.unirules.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService implements SimpleCrud<Long, RoleDto, RoleDto, RoleDto> {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public ApiResponse<RoleDto> create(RoleDto roleDto) {
        Role role = new Role();
        role.setRole(roleDto.role());
        roleRepository.save(role);
        return new ApiResponse<>(
                201,
                "role is saved",
                true,
                roleMapper.toDto(role)
        );
    }

    @Override
    public ApiResponse<RoleDto> get(Long entityId) {
        RoleDto dto = new RoleDto(findRoleById(entityId).getRole());
        return new ApiResponse<>(
                201,
                "role is found by id = %s".formatted(entityId),
                true,
                dto
        );
    }

    public Role findRoleById(Long entityId) {
        return roleRepository.findById(entityId)
                .orElseThrow(() ->
                        new EntityNotFoundException("role is not found by id = %s".formatted(entityId)));
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByRole(roleName).orElseThrow(() -> new EntityNotFoundException(
                "role is not found by this name : %s".formatted(roleName)
        ));
    }

    @Override
    @Transactional
    public ApiResponse<RoleDto> update(Long entityId, RoleDto roleDto) {
        Role role = findRoleById(entityId);
        role.setRole(roleDto.role());
        roleRepository.save(role);
        return new ApiResponse<>(
                200,
                "role is updated",
                true,
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<RoleDto> delete(Long entityId) {
        Role role = findRoleById(entityId);
        roleRepository.delete(role);
        return new ApiResponse<>(
                200,
                "role is deleted",
                true,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<RoleDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all roles",
                true,
                roleRepository.findAll().stream().map(roleMapper::toDto).toList());
    }

    @Override
    public ApiResponse<List<RoleDto>> getAllPagination(Pageable pageable) {
        return new ApiResponse<>(
                200,
                "all roles page",
                true,
                findAllPagination(pageable).map(roleMapper::toDto).toList()
        );
    }

    public Page<Role> findAllPagination(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
