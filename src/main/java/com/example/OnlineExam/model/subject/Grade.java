package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int id;
    @Column(name = "grade")
    @Min(value = 1)
    @Max(value = 6)
    private int grade;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull(message = "user cannot be null")
    private User user;
    @ManyToOne
    @JoinColumn(name = "test_id")
    @NotNull(message = "test cannot be null")
    private Test test;
    @Column(name = "date")
    @NotNull(message = "date cannot be null")
    private LocalDateTime dateTime = LocalDateTime.now();

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public @NotNull Test getTest() {
        return test;
    }

    public void setTest(@NotNull Test test) {
        this.test = test;
    }

    public @NotNull LocalDateTime getDateTime() {
        return dateTime;
    }

}
