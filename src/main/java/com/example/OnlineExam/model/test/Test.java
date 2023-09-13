package com.example.OnlineExam.model.test;

import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private int testId;
    @Column(name = "subject_id")
    private int subject_id;
    @Column(name = "test_creator")
    private int user_id;
    @Column(name = "date")
    private LocalDateTime dateTime;
    @OneToMany(mappedBy = "test",cascade = CascadeType.ALL)
    private List<Question> questions;
    @OneToMany(mappedBy = "grade")
    private List<Grade> grades;
    @ManyToOne()
    @JoinColumn(name = "test_creator")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test(int subject_id, int user_id, LocalDateTime dateTime) {
        this.subject_id = subject_id;
        this.user_id = user_id;
        this.dateTime = dateTime;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getTestId() {
        return testId;
    }
    public void addQuestion(Question question){
        if(this.questions == null){
            this.questions = new ArrayList<>();
        }
        questions.add(question);
        question.setTest(this);
    }

    public void addGrade(Grade grade){
        if(this.grades == null){
            this.grades = new ArrayList<>();
        }
        grades.add(grade);
        grade.setTest(this);
    }
}
