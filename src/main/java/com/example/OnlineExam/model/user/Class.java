package com.example.OnlineExam.model.user;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class")
    private String className;
    @OneToMany(mappedBy = "user")
    private List<User> users;

    public Class(String className, List<User> users) {
        this.className = className;
        this.users = users;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getClassId() {
        return classId;
    }
    public void addUser(User user){
        if(this.users == null){
            users = new ArrayList<>();
        }
        users.add(user);
        user.setClassName(this);
    }
}
