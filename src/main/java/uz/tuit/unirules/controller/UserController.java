package uz.tuit.unirules.controller;


import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateUserReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateUserReqDto;
import uz.tuit.unirules.dto.respond_dto.UserRespDto;
import uz.tuit.unirules.services.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    @PostMapping
    public ApiResponse<UserRespDto> create(@RequestBody CreateUserReqDto createUserReqDto) {
        return userService.create(createUserReqDto);
    }

    
    @GetMapping("/{id}")
    public ApiResponse<UserRespDto> get(@PathVariable(name = "id") Long entityId) {
        return userService.get(entityId);
    }

    @GetMapping("/-student-/{id}")
    public ApiResponse<UserRespDto> getForStudent(@PathVariable(name = "id") Long entityId) {
        return userService.getForStudent(entityId);
    }


    @PutMapping("/{id}")
    public ApiResponse<UserRespDto> update(@PathVariable(name = "id") Long entityId, @RequestBody UpdateUserReqDto updateUserReqDto) {
        return userService.update(entityId, updateUserReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UserRespDto> delete(@PathVariable(name = "id") Long entityId) {
        return userService.delete(entityId);
    }

    @GetMapping("/all")
    public ApiResponse<List<UserRespDto>> getAll() {
        return userService.getAll();
    }

    @GetMapping
    public ApiResponse<Page<UserRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return userService.getAllPagination(pageable);
    }

    @GetMapping("/findByGroupId/{groupId}")
    public ApiResponse<List<UserRespDto>> findUsersByGroupId(@PathVariable Long groupId, @ParameterObject Pageable pageable) {
        return userService.findUsersByGroupId(groupId, pageable);
    }

}
