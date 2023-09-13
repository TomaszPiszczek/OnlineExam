package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.Authority;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue
    @Column(name = "subject_id")
    private int subjectId;
    @Column(name = "subject")
    private String subject;

    @OneToMany(mappedBy = "subject")
    @JoinColumn(name = "subject_id")
    private List<Test> tests;


    public Subject(String subject, List<Test> tests) {
        this.subject = subject;
        this.tests = tests;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

}
