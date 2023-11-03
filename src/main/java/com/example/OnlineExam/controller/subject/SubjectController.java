package com.example.OnlineExam.controller.subject;

import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * This controller is only for Teacher
 */
@RestController
@RequestMapping(value = "api/subject")
public class SubjectController {

    SubjectService subjectService;
    SubjectRepository subjectRepository;

    public SubjectController(SubjectService subjectService, SubjectRepository subjectRepository) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
    }
    @PostMapping("/createSubject")
    public ResponseEntity<String> createSubject(@RequestBody Subject subject){
        subjectService.createSubject(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subject added");
    }

    @PostMapping("/addUsersToSubject")
    public ResponseEntity<String> addUsersToSubject(@RequestBody Set<String> userNames, @RequestParam String subjectName){
        subjectService.addUsersToSubject(userNames,subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users added to subject");
    }

    @PostMapping("/addUserToSubject")
    public ResponseEntity<String> addUserToSubject(@RequestParam String userName,@RequestParam String subjectName){
        subjectService.addUserToSubject(userName,subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added to subject");
    }
    @DeleteMapping("/removeUserFromSubject")
    public ResponseEntity<String> removeUserFromSubject(@RequestBody String userName,@RequestParam String subjectName){
        subjectService.removeUserFromSubject(userName,subjectName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User removed from subject");
    }
    @DeleteMapping("/removeSubject")
    public ResponseEntity<String> removeUserFromSubject(@RequestParam String subjectName){
        subjectService.deleteSubject(subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subject deleted");
    }








}
