package uz.tuit.unirules.controller.faculty;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ApiResponse<GroupRespDto> create(@RequestBody CreateGroupReqDto createGroupReqDto) {
        return groupService.create(createGroupReqDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<GroupRespDto> get(@PathVariable(value = "id") Long entityId) {
        return groupService.get(entityId);
    }

    @PutMapping("/{id}")
    public ApiResponse<GroupRespDto> update(@PathVariable(value = "id") Long entityId,
                                            @RequestBody UpdateGroupReqDto updateGroupReqDto) {
        return groupService.update(entityId, updateGroupReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<GroupRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return groupService.delete(entityId);
    }

    @GetMapping("/all")
    public ApiResponse<List<GroupRespDto>> getAll() {
        return groupService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<GroupRespDto>> getAllPagination(Pageable pageable) {
        return groupService.getAllPagination(pageable);
    }

    @GetMapping("findAllGroups/{educationDirectionId}")
    public ApiResponse<List<GroupRespDto>> findGroupsByEducationDirectionId(@PathVariable(value = "educationDirectionId") Long id, @ParameterObject Pageable pageable) {
        return groupService.getGroupsByEducationDirectionId(id, pageable);
    }
}
