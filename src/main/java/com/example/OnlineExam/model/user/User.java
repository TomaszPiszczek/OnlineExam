package com.example.OnlineExam.model.user;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
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
    @OneToMany(mappedBy = "user")
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

    public void addRole(Authority role){
        if(roles == null){
            roles = new ArrayList<>();
        }
        roles.add(role);
        role.setUser(this);
    }

   


}
