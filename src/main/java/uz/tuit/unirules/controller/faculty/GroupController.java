package uz.tuit.unirules.controller.faculty;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateGroupReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateGroupReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.GroupRespDto;
import uz.tuit.unirules.services.faculty.GroupService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/group")
public class GroupController implements
        SimpleCrud<Long, CreateGroupReqDto, UpdateGroupReqDto, GroupRespDto> {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    @PostMapping
    public ApiResponse<GroupRespDto> create(@RequestBody CreateGroupReqDto createGroupReqDto) {
        return groupService.create(createGroupReqDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<GroupRespDto> get(@PathVariable(value = "id")Long entityId) {
        return groupService.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<GroupRespDto> update(@PathVariable(value = "id")Long entityId,
                                            @RequestBody UpdateGroupReqDto updateGroupReqDto) {
        return groupService.update(entityId,updateGroupReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<GroupRespDto> delete(@PathVariable(value = "id")Long entityId) {
        return groupService.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<GroupRespDto>> getAll() {
        return groupService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<GroupRespDto>> getAllPagination(Pageable pageable) {
        return groupService.getAllPagination(pageable);
    }
}
