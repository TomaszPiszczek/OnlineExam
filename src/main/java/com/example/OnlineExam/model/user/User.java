package com.example.OnlineExam.model.user;

import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "username",unique = true)
    @Length(min = 2, max = 50, message = "Username must be between 2-50 characters")
    @NotNull(message = "Username cannot be blank")
    private String username;
    @Column(name = "password")
    @NotNull(message = "Password cannot be blank")
    @Length(min = 2, max = 70, message = "Password must be between 2-70 characters")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "name")
    @NotNull(message = "Name cannot be blank")
    @Length(min = 2, max = 50, message = "Name must be between 2-50 characters")
    private String name;
    @Column(name = "surname")
    @NotNull(message = "Surname cannot be blank")
    @Length(min = 2, max = 50, message = "Surname must be between 2-50 characters")
    private String surname;
    @Column(name = "email")
    @NotNull(message = "Email cannot be blank")
    @Length(min = 2, max = 50, message = "Email must be between 2-50 characters")
    private String email;
    @OneToMany(mappedBy = "user")
    private Set<Authority> roles;
    @ManyToOne()
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "user_subjects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "student_test",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id")
    )
    private Set<Test> tests;

    @OneToMany(mappedBy = "user")
    List<Grade> grades;




    public User(@NotNull String username, @NotNull String password, boolean enabled, @NotNull String name, @NotNull String surname, @NotNull String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = new HashSet<>();
        this.subjects = new HashSet<>();
        this.tests = new HashSet<>();
    }

    public int getUserId() {
        return userId;
    }

    public Set<Subject> getSubjects() {
        if(subjects==null){
            subjects = new HashSet<>();
        }
        return subjects;
    }


    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }


    public @NotNull String getUsername() {
        return username;
    }


    public Set<Authority> getRoles() {
        if(roles == null) roles = new HashSet<>();
        return roles;
    }

    public void setRoles(Set<Authority> roles) {
        this.roles = roles;
    }

    public void addSubject(Subject subject){
        if(subjects == null){
            subjects = new HashSet<>();
        }
        this.subjects.add(subject);
        subject.getUsers().add(this);
    }
    public void addRole(Authority authority){
        if(roles == null){
            roles = new HashSet<>();
        }
        this.roles.add(authority);
        authority.setUser(this);
    }

    public void removeSubject(Subject subject){
        if(subjects == null){
            subjects = new HashSet<>();
        }
        this.subjects.remove(subject);
        subject.getUsers().remove(this);
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Test> getTests() {

        return tests;
    }

    public void removeTest(Test test){
        if(tests == null){
            tests = new HashSet<>();
        }
        this.tests.remove(test);
        test.getUsers().remove(this);
    }

    public void addTest(Test test){
        if(tests == null){
            tests = new HashSet<>();
        }
        this.tests.add(test);
        test.getUsers().add(this);
    }

}
