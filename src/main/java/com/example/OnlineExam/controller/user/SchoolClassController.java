package com.example.OnlineExam.controller.user;

import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/class")
public class SchoolClassController {
    ClassService classService;

    public SchoolClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/signUserToClass")
    public ResponseEntity<String> signUserToClass(@RequestBody List<String> users, @RequestParam String className){
        classService.saveUsersToClass(users,className);
        return ResponseEntity.ok("Users added to class");
    }
    @DeleteMapping("/removeUserFromClass")
    public ResponseEntity<String> removeUserFromClass(@RequestBody List<String> users, @RequestParam String className){
        classService.removeUserFromClass(users);
        return ResponseEntity.ok("Users added to class");
    }
    @PostMapping("/createClass")
    public ResponseEntity<String> createClass(@RequestBody SchoolClass schoolClass){
        classService.createClass(schoolClass);
        return ResponseEntity.ok("Class created");
    }



}
