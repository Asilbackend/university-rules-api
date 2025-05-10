package uz.tuit.unirules.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.RoleDto;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.services.role.RoleService;

import java.util.List;
@RestController
@RequestMapping("/api/role")
public class RoleController implements SimpleCrud<Long, RoleDto,RoleDto,RoleDto> {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    @PostMapping
    public ApiResponse<RoleDto> create(@RequestBody RoleDto roleDto) {
        return roleService.create(roleDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<RoleDto> get(@PathVariable Long id) {
        return null;
    }

    @Override
    public ApiResponse<RoleDto> update(Long entityId, RoleDto roleDto) {
        return null;
    }

    @Override
    public ApiResponse<RoleDto> delete(Long entityId) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<RoleDto>> getAll() {
        return null;
    }

    @Override
    @GetMapping
    public ApiResponse<List<RoleDto>> getAllPagination(Pageable pageable) {
        return null;
    }
}
