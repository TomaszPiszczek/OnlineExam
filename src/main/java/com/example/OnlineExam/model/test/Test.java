package com.example.OnlineExam.model.test;

import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "test")
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private int id;
    @ManyToOne()
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @Column(name = "test_creator")
    @NotNull(message = "Creator name cannot be null")
    private String testCreator;
    @Column(name = "test_name")
    @NotNull(message = "Test name cannot be null")
    private String testName;
    @Column(name = "date")
    private LocalDateTime dateTime = LocalDateTime.now();
    @OneToMany(mappedBy = "test")
    private Set<Question> questions;
    @OneToMany(mappedBy = "test")
    private List<Grade> grades;
    @ManyToMany(mappedBy = "tests")
    private Set<User> users;

    public int getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public String getTestCreator() {
        return testCreator;
    }

    public void setTestCreator(String testCreator) {
        this.testCreator = testCreator;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }


    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        if(users == null){
            users = new HashSet<>();
        }
        return users;
    }
}
