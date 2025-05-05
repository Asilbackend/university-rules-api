package uz.tuit.unirules.services.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateUserReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateUserReqDto;
import uz.tuit.unirules.dto.respond_dto.UserRespDto;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.faculty.group.Group;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.mapper.UserMapper;
import uz.tuit.unirules.projections.UserProjection;
import uz.tuit.unirules.repository.RoleRepository;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.services.attachment.AttachmentService;
import uz.tuit.unirules.services.discipline_rule.DisciplineRuleService;
import uz.tuit.unirules.services.faculty.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements SimpleCrud<Long, CreateUserReqDto, UpdateUserReqDto, UserRespDto> {
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, GroupService groupService, RoleRepository roleRepository, DisciplineRuleService disciplineRuleService, AttachmentService attachmentService) {
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public ApiResponse<UserRespDto> create(CreateUserReqDto createUserReqDto) {
        if (!createUserReqDto.rePassword().equals(createUserReqDto.password())) {
            throw new RuntimeException("Passwords are not equal");
        }
        //todo: group ni obkelish uchun
        Group group = groupService.findByGroupId(createUserReqDto.groupId());
        // todo: role ni ob kelish uchun
        Optional<Role> role = roleRepository.findByRole(createUserReqDto.role());
        if (userRepository.findUserProjectionByUsername(createUserReqDto.username()).isEmpty()) {
            User user = User.builder()
                    .firstname(createUserReqDto.firstname())
                    .lastname(createUserReqDto.lastname())
                    .email(createUserReqDto.email())
                    .phone(createUserReqDto.phone())
                    .username(createUserReqDto.username())
                    .password(createUserReqDto.password())
                    .build();
            userRepository.save(user);
            UserRespDto respDto = new UserRespDto(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getLanguage(),
                    user.isPassedTest(),
                    group.getId(),
                    role.get().getAuthority());
            return new ApiResponse<>(
                    201,
                    "Foydalanuvchi muvaffaqiyatli yaratildi",
                    true,
                    respDto);
        } else {
            throw new RuntimeException(" already exist username");
        }
    }


    @Override
    public ApiResponse<UserRespDto> get(Long entityId) {
        UserProjection userProjection = userRepository.findUserById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "user is not find by this id = :%s".formatted(entityId)));
        UserRespDto dto = makeUserRespDtoFromUserProjection(userProjection);
        return new ApiResponse<>(
                200,
                "Foydalanuvchi topildi",
                true,
                dto
        );
    }

    private static UserRespDto makeUserRespDtoFromUserProjection(UserProjection userProjection) {
        return new UserRespDto(
                userProjection.getFirstname(),
                userProjection.getLastname(),
                userProjection.getEmail(),
                userProjection.getPhone(),
                userProjection.getLanguage(),
                userProjection.getPassedTest(),
                userProjection.getGroupId(),
                userProjection.getRole());
    }

    public User findByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("user not found"));
    }

    @Override
    @Transactional
    public ApiResponse<UserRespDto> update(Long entityId, UpdateUserReqDto updateUserReqDto) {
        Group group = groupService.findByGroupId(updateUserReqDto.groupId());
        Optional<Role> role = roleRepository.findByRole(updateUserReqDto.role());
        User user = findByUserId(entityId);
        try {
            user.setFirstname(updateUserReqDto.firstname());
            user.setLastname(updateUserReqDto.lastname());
            user.setEmail(updateUserReqDto.email());
            user.setPhone(updateUserReqDto.phone());
            user.setGroup(group);
            user.setRole(role.get());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse<>(
                204,
                "userning malumotlari muvaffaqiyatli update boldi",
                true,
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<UserRespDto> delete(Long entityId) {
        User user = findByUserId(entityId);
        user.setIsDeleted(true);
        userRepository.save(user);
        return new ApiResponse<>(
                200,
                "Foydalanuvchi topildi",
                true,
                null

        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<UserRespDto>> getAll() {
        List<UserRespDto> userRespDtoList = userRepository.findAllUsers(false).stream()
                .map(UserService::makeUserRespDtoFromUserProjection).toList();
        return new ApiResponse<>(
                200,
                "all users",
                true,
                userRespDtoList
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<UserRespDto>> getAllPagination(Pageable pageable) {
        Page<User> allPage = findAllPage(pageable);
        List<User> userList = allPage.getContent().stream().filter(
                user -> user.getIsDeleted().equals(false)).toList();
        List<UserRespDto> dtoList = new ArrayList<>();
        userList.forEach(user -> dtoList.add(new UserRespDto(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhone(),
                user.getLanguage(),
                user.isPassedTest(),
                user.getGroup().getId(),
                user.getRole().getAuthority())));
        return new ApiResponse<>(
                200,
                "all users pages",
                true,
                dtoList
        );
    }

    public Page<User> findAllPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
