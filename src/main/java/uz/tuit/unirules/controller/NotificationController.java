package uz.tuit.unirules.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateNotificationReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateNotificationReqDto;
import uz.tuit.unirules.dto.respond_dto.NotificationRespDto;
import uz.tuit.unirules.services.notification.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<NotificationRespDto> create(@RequestBody CreateNotificationReqDto createNotificationReqDto) {
        return service.create(createNotificationReqDto);
    }

    @Validated
    @GetMapping("/{id}")
    public ApiResponse<NotificationRespDto> get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<NotificationRespDto> update(@PathVariable Long id,
                                                   @RequestBody UpdateNotificationReqDto updateNotificationReqDto) {
        return service.update(id, updateNotificationReqDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<NotificationRespDto> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/all")
    public ApiResponse<List<NotificationRespDto>> getAll() {
        return service.getAll();
    }

    @GetMapping
    public ApiResponse<Page<NotificationRespDto>> getAllPagination(
            @ParameterObject Pageable pageable) {
        return service.getAllPagination(pageable);
    }
}
