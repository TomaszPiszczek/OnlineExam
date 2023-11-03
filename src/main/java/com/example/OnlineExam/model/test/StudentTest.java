package com.example.OnlineExam.model.test;

import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "student_test")
public class StudentTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_test_id")
    private int id;

    @ManyToOne()
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "test_result")
    private Integer testResult;
    @Column(name = "finished")
    @ColumnDefault("false")
    private Boolean finished;

    public int getId() {
        return id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTestResult() {
        return testResult;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public void setTestResult(Integer testResult) {
        this.testResult = testResult;
    }
}
