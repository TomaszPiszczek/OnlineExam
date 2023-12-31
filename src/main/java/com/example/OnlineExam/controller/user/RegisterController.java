package com.example.OnlineExam.controller.user;

import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping(value = "/api/register")
public class RegisterController {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserService userService;

    public RegisterController(PasswordEncoder passwordEncoder, UserRepository userRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/registerStudent")
    public ResponseEntity<String> registerStudent(@RequestBody User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        userRepository.save(user);
        userService.addUserRole(user.getUsername(),"STUDENT");

        return ResponseEntity.status(HttpStatus.CREATED).body("User saved");
    }
    @Transactional
    @PostMapping("/registerTeacher")
    public ResponseEntity<String> registerTeacher(@RequestBody User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        userRepository.save(user);
        userService.addUserRole(user.getUsername(),"TEACHER");

        return ResponseEntity.status(HttpStatus.CREATED).body("User saved");
    }
}
