package uz.tuit.unirules.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.RoleDto;
import uz.tuit.unirules.dto.SimpleCrud;

import java.util.List;
@RequestMapping("/api/role")
public class RoleController implements SimpleCrud<Long, RoleDto,RoleDto,RoleDto> {
    @Override
    public ApiResponse<RoleDto> create(RoleDto roleDto) {
        return null;
    }

    @Override
    public ApiResponse<RoleDto> get(Long entityId) {
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
    public ApiResponse<List<RoleDto>> getAll() {
        return null;
    }

    @Override
    public ApiResponse<List<RoleDto>> getAllPagination(Pageable pageable) {
        return null;
    }
}
