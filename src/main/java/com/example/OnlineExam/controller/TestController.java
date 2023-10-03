package com.example.OnlineExam.controller;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "api")
public class TestController {
    TestService testService;
    UserRepository userRepository;

    //ONLY FOR TEACHERS
    @PostMapping("/createTest")
    public ResponseEntity<String> createTest(@RequestBody Test test){
        testService.createTest(test);

        return ResponseEntity.ok("Test added");
    }
    //ONLY FOR TEACHERS
    @GetMapping("/getTests")
    public ResponseEntity<List<Test>> getTests(@RequestParam String userName){
        List<Test> tests = testService.getTests(userName);

        return null;
    }


}
