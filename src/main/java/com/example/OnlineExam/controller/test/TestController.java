package com.example.OnlineExam.controller.test;

import com.example.OnlineExam.dto.TestDTO;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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
    @GetMapping("/getTests")
    public ResponseEntity<List<TestDTO>> getTests(@RequestParam String userName){
        List<TestDTO> tests = testService.getTests(userName);

        return ResponseEntity.ok(tests);
    }
    @PostMapping("/addUserToTest")
    public ResponseEntity<String> addUserToTest(@RequestParam Integer testId,@RequestBody Set<String> users){
        testService.addUsersToTest(users,testId);
        return ResponseEntity.ok("Users added to test");
    }
    @PostMapping("/addTestForClass")
    public ResponseEntity<String> addTestForClass(@RequestParam String className,@RequestParam Integer testId){
        testService.addTestToClass(className,testId);
        return ResponseEntity.ok("Users added to test");
    }
}
