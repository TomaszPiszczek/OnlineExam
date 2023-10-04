package com.example.OnlineExam.controller;

import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping(value = "/api/register")
public class RegisterController {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public RegisterController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok("User saved");
    }
}
