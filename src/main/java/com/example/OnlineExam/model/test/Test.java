package com.example.OnlineExam.model.test;

import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @ManyToOne
    @JoinColumn(name = "test_creator")
    private User user;
    @Column(name = "test_name")
    private String testName;
    @Column(name = "date")
    private LocalDateTime dateTime = LocalDateTime.now();


    @OneToMany(mappedBy = "test")
    private Set<Question> questions;
    @OneToMany(mappedBy = "test")
    private ArrayList<Grade> grades;


    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

}
