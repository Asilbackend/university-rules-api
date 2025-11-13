package uz.tuit.unirules.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.respond_dto.UserQuestionDto;
import uz.tuit.unirules.dto.respond_dto.UserQuestionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.repository.test.TestRepository;
import uz.tuit.unirules.services.user_test.UserTestService;

import java.util.List;

@RestController
@RequestMapping("/api/student/test")
@RequiredArgsConstructor
public class TestPageController {
    private final UserTestService userTestService;
    private final TestRepository testRepository;

    @GetMapping("/start")
    public void startTest(@RequestParam Long moduleId) {
        userTestService.startTest(moduleId);
    }

    @GetMapping("/userQuestionsNumbers")
    public List<UserQuestionDto> getUserQuestionsNumber(@RequestParam Long moduleId) {
        return userTestService.getUserQuestions(moduleId);
    }

    @GetMapping("/userQuestion")
    public HttpEntity<UserQuestionRespDto> getUserQuestions(@RequestParam Long questionId) {
        return ResponseEntity.ok(userTestService.getUserQuestion(questionId));
    }

    @GetMapping("/userQuestion/byTestId")
    public HttpEntity<List<UserQuestionRespDto>> getUserQuestionsByTestId(@RequestParam Long testId) {
        Test test = testRepository.findById(testId).orElseThrow();
        List<UserQuestionRespDto> list = test.getQuestions().stream().map(q -> userTestService.getUserQuestion(q.getId())).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/finish")
    public HttpEntity<?> finishTest(@RequestParam Long testId) {
        userTestService.finishTest(testId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/answer")
    public HttpEntity<?> answerQuestion(@RequestParam Long questionOptionId) {
        userTestService.answer(questionOptionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
