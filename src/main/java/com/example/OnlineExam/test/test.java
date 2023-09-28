package com.example.OnlineExam.test;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.service.ClassService;
import com.example.OnlineExam.service.TestService;
import com.example.OnlineExam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class test {
    PasswordEncoder passwordEncoder;
    UserService userService;
    ClassService classService;
    TestRepository testRepository;
    TestService testService;

    public test(PasswordEncoder passwordEncoder, UserService userService, ClassService classService, TestRepository testRepository, TestService testService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.classService = classService;
        this.testRepository = testRepository;
        this.testService = testService;
    }




    @GetMapping("/student")
    public String  user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "Witaj, " + username + "!";
    }
    @GetMapping("/test")
    public String  test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining());
        return "Witaj, " + username + "!";
    }

    @GetMapping("/teacher")
    public String  teacher(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "Witaj, " + username + "!";
    }
    @PostMapping("/addUserToClass")
    public String addUserToClass(@RequestParam String username, @RequestParam String className){

        classService.saveUserToClass(username,className);
        return "dodano";
    }
    @PostMapping("/saveUser")
    public String addUserToClass(@RequestParam String username, @RequestParam String password,@RequestParam String name,@RequestParam String surname,@RequestParam String email,@RequestParam(required=false) String classname ,@RequestParam String role){
        User user = new User(username, passwordEncoder.encode(password),true,name,surname,email);
        userService.saveUser(user);
        userService.addUserRole(username,role);


        if(classname !=null){
            classService.saveUserToClass(username,classname);
        }
        return "dodano";
    }

    @PostMapping("/createTest")
    public ResponseEntity<String> createTest(@RequestBody Test test){


        testService.createTest(test);

        return ResponseEntity.ok("Test added");
    }

}
