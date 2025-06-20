package uz.tuit.unirules.services.support_request;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.respond_dto.SupportReqRespDto;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.support_request.SupportRequest;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.SupportRequestRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.user.UserService;

import java.util.List;

@Service
public class SupportRequestService {
    private final SupportRequestRepository supportRequestRepository;
    private final UserService userService;
    private final AuthUserService authUserService;

    public SupportRequestService(SupportRequestRepository supportRequestRepository, UserService userService, AuthUserService authUserService) {
        this.supportRequestRepository = supportRequestRepository;
        this.userService = userService;
        this.authUserService = authUserService;
    }


    public void sendReq(Long userId, String subject, String message) {
        User user = userService.findByUserId(userId);
        supportRequestRepository.save(
                SupportRequest.builder()
                        .status(SupportRequest.Status.OPEN)
                        .isDeleted(false)
                        .subject(subject)
                        .message(message)
                        .user(user)
                        .status(SupportRequest.Status.OPEN)
                        .build()
        );
    }

    public void sendResp(Long supportUserId, Long supportReqId, String respMessage) {
        SupportRequest supportRequest = findById(supportReqId);
        User supportUser = userService.findByUserId(supportUserId);
        supportRequest.setResponseMessage(respMessage);
        supportRequest.setSupportUser(supportUser);
        supportRequest.setStatus(SupportRequest.Status.CLOSED);
        supportRequestRepository.save(supportRequest);
    }

    private SupportRequest findById(Long supportReqId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportReqId).orElseThrow(() -> new EntityNotFoundException("supportRequest not found by id = %s".formatted(supportReqId)));
        User authUser = authUserService.getAuthUser();
        if (authUser != null) {
            Role role = authUser.getRole();
            if (role != null && role.getRole().equals("ADMIN"))
                if (supportRequest.getStatus().equals(SupportRequest.Status.OPEN)) {
                    supportRequest.setStatus(SupportRequest.Status.PENDING);
                    supportRequestRepository.save(supportRequest);
                }
        }
        return supportRequest;
    }


    public SupportReqRespDto getById(Long supportReqId) {
        SupportRequest supportRequest = findById(supportReqId);
        return makeRespDto(supportRequest);
    }

    private static SupportReqRespDto makeRespDto(SupportRequest supportRequest) {
        return new SupportReqRespDto(
                supportRequest.getId(),
                supportRequest.getCreatedAt(),
                supportRequest.getUpdatedAt(),
                supportRequest.getUser().getId(),
                supportRequest.getSupportUser().getId(),
                supportRequest.getSubject(),
                supportRequest.getMessage(),
                supportRequest.getResponseMessage(),
                supportRequest.getIsDeleted(),
                supportRequest.getStatus()
        );
    }

    public List<SupportReqRespDto> getAll() {
        List<SupportRequest> supportRequests = supportRequestRepository.findAll();
        return supportRequests.stream().map(SupportRequestService::makeRespDto).toList();
    }

    public List<SupportReqRespDto> getAllPageable(Pageable pageable) {
        return supportRequestRepository.findAll(pageable).map(SupportRequestService::makeRespDto).toList();
    }

    public List<SupportReqRespDto> getAllByStudentId(Long studentId, SupportRequest.Status status, Pageable pageable) {
        if (!authUserService.getAuthUserId().equals(studentId)) {
            throw new RuntimeException("ushbu student ushbu malumotni olish huquqiga ega emas");
        }
        Page<SupportRequest> supportRequests;
        if (status == null) {
            supportRequests = supportRequestRepository.findAllByUserIdAndIsDeletedFalse(studentId, pageable);
        } else {
            supportRequests = supportRequestRepository.findAllByUserIdAndIsDeletedFalseAndStatus(studentId, status, pageable);
        }
        return supportRequests.map(SupportRequestService::makeRespDto).toList();
    }

    public List<SupportReqRespDto> getAllBySupportId(Long supportId, SupportRequest.Status status, Pageable pageable) {
        Page<SupportRequest> page;
        if (status == null) {
            page = supportRequestRepository.findAllBySupportUserIdAndIsDeletedFalse(supportId, pageable);
        } else {
            page = supportRequestRepository.findAllBySupportUserIdAndIsDeletedFalseAndStatus(supportId, status, pageable);
        }
        return page.map(SupportRequestService::makeRespDto).toList();
    }

    public SupportReqRespDto getByIdForStudent(Long supportReqId, Long studentId) {
        if (!authUserService.getAuthUserId().equals(studentId))
            throw new RuntimeException("ushbu student ushbu malumotni olish huquqiga ega emas");
        return makeRespDto(findById(supportReqId));
    }
}
