package com.example.OnlineExam.model.user;

import com.example.OnlineExam.model.subject.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
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
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Column(name = "password")
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 2, max = 50, message = "Password must be between 2-50 characters")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "name")
    @NotBlank(message = "Name cannot be blank")
    @Length(min = 2, max = 50, message = "Name must be between 2-50 characters")
    private String name;
    @Column(name = "surname")
    @NotBlank(message = "Surname cannot be blank")
    @Length(min = 2, max = 50, message = "Surname must be between 2-50 characters")
    private String surname;
    @Column(name = "email")
    @NotBlank(message = "Email cannot be blank")
    @Length(min = 2, max = 50, message = "Email must be between 2-50 characters")
    private String email;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Authority> roles;
    @ManyToOne()
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "user_subjects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;


    public User(String username, String password, boolean enabled, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = new ArrayList<>();
        this.subjects = new HashSet<>();
    }


    public Set<Subject> getSubjects() {
        if(subjects==null){
            subjects = new HashSet<>();
        }
        return subjects;
    }

    public void setSubjects(HashSet<Subject> subjects) {
        this.subjects = subjects;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }


    public List<Authority> getRoles() {
        return roles;
    }

    public void setRoles(List<Authority> roles) {
        this.roles = roles;
    }

    public void addSubject(Subject subject){
        if(subjects == null){
            subjects = new HashSet<>();
        }
        this.subjects.add(subject);
        subject.getUsers().add(this);
    }
    public void removeSubject(Subject subject){
        if(subjects == null){
            subjects = new HashSet<>();
        }
        this.subjects.remove(subject);
        subject.getUsers().remove(this);
    }


}
