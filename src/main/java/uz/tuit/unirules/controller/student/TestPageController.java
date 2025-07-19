package uz.tuit.unirules.controller.student;

import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.tuit.unirules.entity.user_test.UserTest;
import uz.tuit.unirules.services.test.TestService;
import uz.tuit.unirules.services.user_test.UserTestService;

@RestController
@RequestMapping("/api/student/test")
@RequiredArgsConstructor
public class TestPageController {
    private final UserTestService userTestService;

    @GetMapping("/start")
    public void startTest( @RequestParam Long moduleId) {
        userTestService.startTest(moduleId);
    }

}
