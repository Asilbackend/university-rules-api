package uz.tuit.unirules.services.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.CreateUserReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateUserReqDto;
import uz.tuit.unirules.dto.respond_dto.UserRespDto;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.mapper.UserMapper;
import uz.tuit.unirules.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements SimpleCrud<Long, CreateUserReqDto, UpdateUserReqDto, UserRespDto> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ApiResponse<UserRespDto> create(CreateUserReqDto createUserReqDto) {
        if (!createUserReqDto.rePassword().equals(createUserReqDto.password())) {
            throw new RuntimeException("Passwords are not equal");
        }
        if (userRepository.findByUsername(createUserReqDto.username()).isEmpty()) {
            User user = User.builder()
                    .firstname(createUserReqDto.firstname())
                    .lastname(createUserReqDto.lastname())
                    .email(createUserReqDto.email())
                    .phone(createUserReqDto.phone())
                    .username(createUserReqDto.username())
                    .password(createUserReqDto.password())
                    .build();
              userRepository.save(user);
            return new ApiResponse<>(
                    201,
                    "Foydalanuvchi muvaffaqiyatli yaratildi",
                    true,
                    userMapper.toRespDto(user));
        }
        else {
            throw new RuntimeException(" already exist username");
        }
    }


    @Override
    public ApiResponse<UserRespDto> get(Long entityId) {
        User user = findByUserId(entityId);
        UserRespDto dto = userMapper.toRespDto(user);
        return new ApiResponse<>(
                200,
                "Foydalanuvchi topildi",
                true,
                dto
        );
    }

    public User findByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("user not found"));
    }

    @Override
    public ApiResponse<UserRespDto> update(Long entityId, UpdateUserReqDto updateUserReqDto) {
        User user = findByUserId(entityId);
        user.setFirstname(updateUserReqDto.firstname());
        user.setLastname(updateUserReqDto.lastname());
        user.setEmail(updateUserReqDto.email());
        user.setPhone(updateUserReqDto.phone());
        userRepository.save(user);
        return new ApiResponse<>(
                204,
                "userning malumotlari muvaffaqiyatli update boldi",
                true,
                null
        );
    }

    @Override
    public ApiResponse<UserRespDto> delete(Long entityId) {
        User user=findByUserId(entityId);
        userRepository.delete(user);
        return new ApiResponse<>(
                200,
                "Foydalanuvchi topildi",
                true,
                null

        ) ;
    }
    @Override
    public ApiResponse<List<UserRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "all users",
                true,
                userRepository.findAll().stream().map(userMapper::toRespDto).toList()
        );
    }

    @Override
    public ApiResponse<List<UserRespDto>> getAllPagination(Pageable pageable) {
        Page<User> allPage = findAllPage(pageable);
        List<UserRespDto> list = allPage.map(userMapper::toRespDto).toList();
        return new ApiResponse<>(
                200,
                "all users pages",
                true,
                list
        );
    }
    public Page<User> findAllPage(Pageable pageable){
        return userRepository.findAll(pageable);
    }
}
