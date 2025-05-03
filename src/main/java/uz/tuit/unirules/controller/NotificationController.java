package uz.tuit.unirules.controller;

import org.springframework.data.domain.Pageable;
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
public class NotificationController implements
        SimpleCrud<Long, CreateNotificationReqDto, UpdateNotificationReqDto, NotificationRespDto> {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ApiResponse<NotificationRespDto> create(@RequestBody CreateNotificationReqDto createNotificationReqDto) {
        return service.create(createNotificationReqDto);
    }

    @Override
    @Validated
    @GetMapping("/{id}")
    public ApiResponse<NotificationRespDto> get(@PathVariable(value =  "id") Long entityId) {
        return service.get(entityId);
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<NotificationRespDto> update(@PathVariable(value = "id") Long entityId,
                                                   @RequestBody UpdateNotificationReqDto updateNotificationReqDto) {
        return service.update(entityId, updateNotificationReqDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<NotificationRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return service.delete(entityId);
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<List<NotificationRespDto>> getAll() {
        return service.getAll();
    }

    @Override
    @GetMapping
    public ApiResponse<List<NotificationRespDto>> getAllPagination(Pageable pageable) {
        return service.getAllPagination(pageable);
    }
}
