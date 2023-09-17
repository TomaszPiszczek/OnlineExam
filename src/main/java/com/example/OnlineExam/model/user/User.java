package com.example.OnlineExam.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;
//todo ERROR VALIDATION FOR WRONG PASSWORD AND USERNAME. EXCEPTION HANDLING, IMPROVE TESTS
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


    public User(String username, String password, boolean enabled, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.email = email;

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


}
