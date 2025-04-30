package uz.tuit.unirules.services.notification;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateNotificationReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateNotificationReqDto;
import uz.tuit.unirules.dto.respond_dto.NotificationRespDto;
import uz.tuit.unirules.entity.notification.Notification;
import uz.tuit.unirules.mapper.NotificationMapper;
import uz.tuit.unirules.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService implements
        SimpleCrud<Long, CreateNotificationReqDto, UpdateNotificationReqDto, NotificationRespDto> {
    private final NotificationMapper mapper;
    private final NotificationRepository repository;

    public NotificationService(NotificationMapper mapper, NotificationRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public ApiResponse<NotificationRespDto> create(CreateNotificationReqDto createNotificationReqDto) {
        return null;
    }

    @Override
    public ApiResponse<NotificationRespDto> get(Long entityId) {
        Notification notification=findById(entityId);
        return new ApiResponse<>(
                200,
                "notification is found",
                true,
                mapper.toDto(notification)
        );
    }
    public Notification findById(Long entityId){
        return repository.findById(entityId)
                .orElseThrow(()->new EntityNotFoundException("notification is not found by id"));
    }
    @Override
    public ApiResponse<NotificationRespDto> update(Long entityId,
                                                   UpdateNotificationReqDto updateNotificationReqDto) {
        Notification notification=findById(entityId);
        notification.setMessage(updateNotificationReqDto.message());
        notification.setTitle(updateNotificationReqDto.title());
        notification.setIs_read(updateNotificationReqDto.is_read());
        repository.save(notification);
        return new ApiResponse<>(
                200,
                "notification is updated",
                true,
                mapper.toDto(notification)
        );
    }

    @Override
    public ApiResponse<NotificationRespDto> delete(Long entityId) {
        Notification notification=findById(entityId);
        repository.delete(notification);
        return new ApiResponse<>(
                200,
                "notification is deleted",
                true,
                null
        );
    }

    @Override
    public ApiResponse<List<NotificationRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all Notifications",
                true,
                repository.findAll().stream().map(mapper::toDto).toList()
        );
    }

    @Override
    public ApiResponse<List<NotificationRespDto>> getAllPagination(Pageable pageable) {
        return new ApiResponse<>(
                200,
                "all notifications page",
                true,
                findAllPage(pageable).map(mapper::toDto).toList()
        );
    }
    public Page<Notification> findAllPage(Pageable pageable){
        return repository.findAll(pageable);
    }
}
