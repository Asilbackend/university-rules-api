package uz.tuit.unirules.services.user_test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.entity.user_test.UserTest;
import uz.tuit.unirules.entity.user_test.UserTestRepository;
import uz.tuit.unirules.handler.exceptions.CustomException;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.test.TestService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTestService {
    private final AuthUserService authUserService;
    private final UserTestRepository userTestRepository;
    private final TestService testService;

    public void startTest(Long moduleId) {
        User authUser = authUserService.getAuthUser();
        Test test = testService.findByModuleId(moduleId);
        Optional<UserTest> userTest = userTestRepository.findByUserIdAndTestId(authUser.getId(), test.getId());
        if (userTest.isEmpty()) {
            createUserTest(authUser, test);
        }
    }

    private UserTest createUserTest(User authUser, Test test) {
        return userTestRepository.save(UserTest.builder()
                .isDeleted(false)
                .success(null)
                .test(test)
                .startedAt(LocalDateTime.now())
                .user(authUser)
                .build());
    }

    public void finishTest(Long testId) {
        User authUser = authUserService.getAuthUser();
        UserTest userTest = userTestRepository.findByUserIdAndTestId(authUser.getId(), testId)
                .orElseThrow(() -> new RuntimeException("User Test topilmadi"));
        if (isTestTimeOver(userTest)) {
            throw new CustomException("Testning amal qilish muddati tugagan", HttpStatus.GONE, "TEST_DEADLINE_EXPIRED");
        }
        if (userTest.getFinishedAt() != null) {
            throw new CustomException("Test allaqachon yakunlangan", HttpStatus.FORBIDDEN, "TEST_ALREADY_FINISHED");
        }
        userTest.setFinishedAt(LocalDateTime.now());
        calculateResult(userTest);
        userTestRepository.save(userTest);
    }

    private void calculateResult(UserTest userTest) {
        userTest.setSuccess(true); // yoki false, natijaga qarab
    }


    public boolean isTestTimeOver(UserTest userTest) {
        LocalDateTime startedAt = userTest.getStartedAt();
        int durationSeconds = userTest.getTest().getDurationSecond();
        return LocalDateTime.now().isAfter(startedAt.plusSeconds(durationSeconds));
    }

}
