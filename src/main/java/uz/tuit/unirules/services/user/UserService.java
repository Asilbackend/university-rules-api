package uz.tuit.unirules.services.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateUserReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateUserReqDto;
import uz.tuit.unirules.dto.respond_dto.UserRespDto;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;
import uz.tuit.unirules.entity.faculty.group.Group;
import uz.tuit.unirules.entity.user.User;

import uz.tuit.unirules.entity.user_image.UserImage;
import uz.tuit.unirules.projections.UserProjection;
import uz.tuit.unirules.repository.UserImageRepository;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.faculty.GroupService;
import uz.tuit.unirules.services.role.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final RoleService roleService;
    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRepository userImageRepository;

    @Transactional
    public ApiResponse<UserRespDto> create(CreateUserReqDto createUserReqDto) {
        if (!createUserReqDto.rePassword().equals(createUserReqDto.password())) {
            throw new RuntimeException("Passwords are not equal");
        }
        //todo: group ni obkelish uchun
        Group group = groupService.findByGroupId(createUserReqDto.groupId());
        // todo: role ni ob kelish uchun
        Role role = roleService.findRoleByName(createUserReqDto.role());
        if (userRepository.findUserProjectionByUsername(createUserReqDto.username()).isEmpty()) {
            User user = User.builder()
                    .firstname(createUserReqDto.firstname())
                    .lastname(createUserReqDto.lastname())
                    .email(createUserReqDto.email())
                    .phone(createUserReqDto.phone())
                    .username(createUserReqDto.username())
                    .password(passwordEncoder.encode(createUserReqDto.password()))
                    .group(group)
                    .role(role)
                    .build();
            userRepository.save(user);
            UserRespDto respDto = new UserRespDto(
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getUsername(),
                    user.getLanguage(),
                    user.isPassedTest(),
                    group.getId(),
                    role.getRole());
            return new ApiResponse<>(
                    201,
                    "user is saved successfully",
                    true,
                    respDto);
        } else {
            throw new RuntimeException(" already exist username");
        }
    }


    public ApiResponse<UserRespDto> get(Long entityId) {
        UserProjection userProjection = userRepository.findUserProjById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "user is not find by this id = %s".formatted(entityId)));
        UserRespDto dto = makeUserRespDtoFromUserProjection(userProjection);
        return new ApiResponse<>(
                200,
                "user is found successfully",
                true,
                dto
        );
    }

    private static UserRespDto makeUserRespDtoFromUserProjection(UserProjection userProjection) {
        return new UserRespDto(
                userProjection.getId(),
                userProjection.getFirstname(),
                userProjection.getLastname(),
                userProjection.getEmail(),
                userProjection.getPhone(),
                userProjection.getUsername(),
                userProjection.getLanguage(),
                userProjection.getPassedTest(),
                userProjection.getGroupId(),
                userProjection.getRole());
    }

    public User findByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("user not found"));
    }

    @Transactional
    public ApiResponse<UserRespDto> update(Long entityId, UpdateUserReqDto updateUserReqDto) {
        Group group = groupService.findByGroupId(updateUserReqDto.groupId());
        Role role = roleService.findRoleByName(updateUserReqDto.role());
        User user = findByUserId(entityId);
        try {
            user.setFirstname(updateUserReqDto.firstname());
            user.setLastname(updateUserReqDto.lastname());
            user.setEmail(updateUserReqDto.email());
            user.setPhone(updateUserReqDto.phone());
            user.setGroup(group);
            user.setRole(role);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse<>(
                204,
                "user's information are updated successfully",
                true,
                null
        );
    }

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

    @Transactional(readOnly = true)
    public ApiResponse<Page<UserRespDto>> getAllPagination(Pageable pageable) {
        Page<UserProjection> projections = userRepository.findAllUsersPages(pageable);
        Page<UserRespDto> dtoPage = projections.map(UserService::makeUserRespDtoFromUserProjection);
        return new ApiResponse<>(
                200,
                "all users pages",
                true,
                dtoPage
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<UserRespDto>> findUsersByGroupId(Long groupId, Pageable pageable) {
        Group group = groupService.findByGroupId(groupId); // bu faqat tekshirish uchun

        Page<UserProjection> usersPage = userRepository.findUsersByGroupId(groupId, pageable);
        List<UserRespDto> dtoList = usersPage.stream().map(user -> new UserRespDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                user.getLanguage(),
                user.getPassedTest(),
                user.getGroupId(),
                user.getRole()
        )).toList();
        return new ApiResponse<>(
                200,
                "Users are found by group id = %s".formatted(groupId),
                true,
                dtoList
        );
    }

    public Page<User> findAllPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public ApiResponse<UserRespDto> getForStudent(Long entityId) {
        if (!authUserService.getAuthUserId().equals(entityId)) {
            throw new RuntimeException("ruxsat etilmagan qiymat ");
        }
        return get(entityId);
    }

    public Map<String, Object> getStudentData() {
        User user = authUserService.getAuthUser();
        Map<String, Object> data = new HashMap<>();

        // Foydalanuvchi ma'lumotlarini xavfsiz olish
        data.put("firstname", Optional.ofNullable(user.getFirstname()).orElse(""));
        data.put("lastname", Optional.ofNullable(user.getLastname()).orElse(""));
        data.put("lang", Optional.ofNullable(user.getLanguage()).orElse("uz"));

        // Guruh va yo‘nalish xavfsiz tekshiruvi
        Group group = user.getGroup();
        if (group != null) {
            data.put("groupName", Optional.ofNullable(group.getName()).orElse("Noma’lum guruh"));

            EducationDirection direction = group.getEducationDirection();
            data.put("direction", direction != null
                    ? Optional.ofNullable(direction.getName()).orElse("Noma’lum yo‘nalish")
                    : "Noma’lum yo‘nalish");
        } else {
            data.put("groupName", "Guruh belgilanmagan");
            data.put("direction", "Yo‘nalish belgilanmagan");
        }

        // Foydalanuvchi rasmi
        Optional<Attachment> optionalAttachment =
                userImageRepository.findByUserIdOrderByIdDescDeletedFalse(user.getId());

        optionalAttachment.ifPresentOrElse(
                attachment -> {
                    data.put("userImageUrl", attachment.getUrl());
                    data.put("userImageId", attachment.getId());
                },
                () -> {
                    data.put("userImageUrl", null);
                    data.put("userImageId", null);
                }
        );

        return data;
    }

}
