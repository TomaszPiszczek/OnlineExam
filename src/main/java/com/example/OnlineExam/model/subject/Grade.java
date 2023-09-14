package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int gradeId;
    @Column(name = "grade")
    private int grade;
    /*@Column(name = "user_id")
    private int userId;*/
    /*@Column(name = "test_id")
    private int testId;*/
    @Column(name = "date")
    private LocalDateTime dateTime;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "test_id")
    private Test test;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Grade(int grade, int userId, int testId, LocalDateTime dateTime) {
        this.grade = grade;
        //this.userId = userId;
        //this.testId = testId;
        this.dateTime = dateTime;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

   /* public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
*/
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getGradeId() {
        return gradeId;
    }

}
