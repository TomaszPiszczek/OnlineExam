package com.example.OnlineExam.controller;

import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        //todo add user creator to subject
    @PostMapping("/createSubject")
    public ResponseEntity<String> createSubject(@RequestBody Subject subject){
        subjectRepository.save(subject);

        return ResponseEntity.ok("Subject added");
    }

    @PostMapping("/addUsersToSubject")
    public ResponseEntity<String> addUsersToSubject(@RequestBody List<String> userNames,@RequestParam String subjectName){
        subjectService.addUsersToSubject(userNames,subjectName);
        return ResponseEntity.ok("Users added to subject");
    }

    @PostMapping("/addUserToSubject")
    public ResponseEntity<String> addUserToSubject(@RequestParam String userName,@RequestParam String subjectName){
        subjectService.addUserToSubject(userName,subjectName);
        return ResponseEntity.ok("User added to subject");
    }
    @DeleteMapping("/removeUserFromSubject")
    public ResponseEntity<String> removeUserFromSubject(@RequestBody String userName,@RequestParam String subjectName){
        subjectService.removeUserFromSubject(userName,subjectName);
        return ResponseEntity.ok("User removed from subject");
    }
    @DeleteMapping("/removeSubject")
    public ResponseEntity<String> removeUserFromSubject(@RequestParam String subjectName){
        subjectService.deleteSubject(subjectName);
        return ResponseEntity.ok("Subject deleted");
    }








}
