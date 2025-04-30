package uz.tuit.unirules.controller;


import org.springdoc.core.annotations.ParameterObject;
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
public class UserController implements SimpleCrud<Long, CreateUserReqDto, UpdateUserReqDto,UserRespDto> {
   private final   UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Override
    @PostMapping
    public ApiResponse<UserRespDto> create(@RequestBody CreateUserReqDto createUserReqDto) {
        return userService.create(createUserReqDto);
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<UserRespDto> get(@PathVariable(name = "id") Long entityId) {
        return userService.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<UserRespDto> update(@PathVariable(name = "id") Long entityId, @RequestBody UpdateUserReqDto updateUserReqDto) {
        return userService.update(entityId,updateUserReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<UserRespDto> delete(@PathVariable(name = "id") Long entityId) {
        return userService.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<UserRespDto>> getAll() {
        return userService.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<UserRespDto>> getAllPagination(@ParameterObject Pageable pageable) {
        return userService.getAllPagination(pageable);
    }
}
