package com.example.OnlineExam.controller.test;

import com.example.OnlineExam.dto.TestDTO;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.test.TestEvaluator;
import com.example.OnlineExam.service.test.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;



@RestController
@RequestMapping(value = "api/test")
public class TestController {
    TestService testService;
    UserRepository userRepository;
    TestEvaluator testEvaluator;

    public TestController(TestService testService, UserRepository userRepository, TestEvaluator testEvaluator) {
        this.testService = testService;
        this.userRepository = userRepository;
        this.testEvaluator = testEvaluator;
    }

    /**
     * This method is only for Teachers
     */
    @PostMapping("/createTest")
    public ResponseEntity<String> createTest(@RequestBody Test test){
        testService.createTest(test);

        return ResponseEntity.status(HttpStatus.CREATED).body("Test added");
    }
    @GetMapping("/getTests")
    public ResponseEntity<List<TestDTO>> getTests(@RequestParam String userName){
        List<TestDTO> tests = testService.getTests(userName);

        return ResponseEntity.ok(tests);
    }
    @PostMapping("/addUserToTest")
    public ResponseEntity<String> addUserToTest(@RequestParam Integer testId,@RequestBody Set<String> users){
        testService.addUsersToTest(users,testId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users added to test");
    }
    @PostMapping("/addTestForClass")
    public ResponseEntity<String> addTestForClass(@RequestParam String className,@RequestParam Integer testId){
        testService.addTestToClass(className,testId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users added to test");
    }
    @PostMapping("/rateTest")
    public ResponseEntity<String> rateTest(@RequestBody List<Question> questions, @RequestParam String userName, Integer testId){
    int score = testEvaluator.RateTest(questions,userName,testId);
    return ResponseEntity.status(HttpStatus.CREATED).body("Test saved with score : " + score);
    }

}
