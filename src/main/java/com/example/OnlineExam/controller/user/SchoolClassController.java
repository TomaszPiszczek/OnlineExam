package com.example.OnlineExam.controller.user;

import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.service.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/class")
public class SchoolClassController {
    SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @PostMapping("/signUserToClass")
    public ResponseEntity<String> signUserToClass(@RequestBody Set<String> users, @RequestParam String className){
        schoolClassService.saveUsersToClass(users,className);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users added to class");
    }
    @DeleteMapping("/removeUserFromClass")
    public ResponseEntity<String> removeUserFromClass(@RequestBody List<String> users, @RequestParam String className){
        schoolClassService.removeUserFromClass(users);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Users removed from class");
    }
    @PostMapping("/createClass")
    public ResponseEntity<String> createClass(@RequestBody SchoolClass schoolClass){
        schoolClassService.createClass(schoolClass);
        return ResponseEntity.status(HttpStatus.CREATED).body("Class created");
    }



}
