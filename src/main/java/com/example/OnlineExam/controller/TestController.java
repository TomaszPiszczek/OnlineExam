package com.example.OnlineExam.controller;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//todo testCreator change to users , testCreator get by userRole = teacher
//todo getTestCreator endppoint

@RestController
@RequestMapping(value = "api/test")
public class TestController {
    TestService testService;
    UserRepository userRepository;

    public TestController(TestService testService, UserRepository userRepository) {
        this.testService = testService;
        this.userRepository = userRepository;
    }


    /**
     * This method is only for Teachers
     */
    @PostMapping("/createTest")
    public ResponseEntity<String> createTest(@RequestBody Test test){
        testService.createTest(test);

        return ResponseEntity.ok("Test added");
    }
    //fixme TestDAO return from getTests
    @GetMapping("/getTests")
    public ResponseEntity<List<Test>> getTests(@RequestParam String userName){
        List<Test> tests = testService.getTests(userName);

        return ResponseEntity.ok(tests);
    }


}
