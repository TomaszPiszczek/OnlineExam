package com.example.OnlineExam.model.user;

import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Authority> roles;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Grade> grades;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Test> tests;
    @ManyToOne()
    @JoinColumn(name = "class_id")
    private Class className;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_subject",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public User(String username, String password, boolean enabled, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }



    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Authority> getRoles() {
        return roles;
    }

    public void setRoles(List<Authority> roles) {
        this.roles = roles;
    }
    public void addRole(Authority authority){
        if(roles ==null){
            roles = new ArrayList<>();
        }
        roles.add(authority);
        authority.setUser(this);
    }

    public void addGrade(Grade grade){
        if(this.grades == null){
            this.grades = new ArrayList<>();
        }
        grades.add(grade);
        grade.setUser(this);
    }

    public void addGrade(Test test){
        if(this.tests == null){
            this.tests = new ArrayList<>();
        }
        tests.add(test);
        test.setUser(this);
    }
    public void addSubject(Subject subject){
        if (this.subjects == null){
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }
}
