package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_subject",
               joinColumns = @JoinColumn(name = "subject_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;



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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
    }

}
