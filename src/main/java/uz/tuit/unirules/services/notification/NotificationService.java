package uz.tuit.unirules.services.notification;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateNotificationReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateNotificationReqDto;
import uz.tuit.unirules.dto.respond_dto.NotificationRespDto;
import uz.tuit.unirules.entity.notification.Notification;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.mapper.NotificationMapper;
import uz.tuit.unirules.repository.NotificationRepository;
import uz.tuit.unirules.services.user.UserService;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final UserService userService;

    public NotificationService(NotificationMapper mapper, NotificationRepository repository, UserService userService) {
        this.mapper = mapper;
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public ApiResponse<NotificationRespDto> create(CreateNotificationReqDto createNotificationReqDto) {
        //user ob kelish
        User user = userService.findByUserId(createNotificationReqDto.userId());

        Notification notification = Notification.builder()
                .is_read(createNotificationReqDto.is_read())
                .message(createNotificationReqDto.message())
                .title(createNotificationReqDto.title())
                .user(user)
                .build();
        repository.save(notification);
        return new ApiResponse<>(
                201,
                "Notification is saved",
                true,
                mapper.toDto(notification)
        );
    }

    public ApiResponse<NotificationRespDto> get(Long entityId) {
        Notification notification = findById(entityId);
        return new ApiResponse<>(
                200,
                "notification is found",
                true,
                mapper.toDto(notification)
        );
    }

    public Notification findById(Long entityId) {
        return repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("notification is not found by id"));
    }

    public ApiResponse<NotificationRespDto> update(Long entityId,
                                                   UpdateNotificationReqDto updateNotificationReqDto) {
        Notification notification = findById(entityId);
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

    public ApiResponse<NotificationRespDto> delete(Long entityId) {
        Notification notification = findById(entityId);
        repository.delete(notification);
        return new ApiResponse<>(
                200,
                "notification is deleted",
                true,
                null
        );
    }

    public ApiResponse<List<NotificationRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all Notifications",
                true,
                repository.findAll().stream().map(mapper::toDto).toList()
        );
    }

    public ApiResponse<Page<NotificationRespDto>> getAllPagination(Pageable pageable) {
        Page<Notification> notificationPage = repository.findAllByIsDeletedFalse(pageable);
        Page<NotificationRespDto> dtoPage = notificationPage.map(mapper::toDto);
        return new ApiResponse<>(
                200,
                "all notifications page",
                true,
                dtoPage
        );
    }

    public Page<Notification> findAllPage(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
