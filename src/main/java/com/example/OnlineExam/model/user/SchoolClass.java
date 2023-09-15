package com.example.OnlineExam.model.user;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class")
    private String name;
    @OneToMany(mappedBy = "schoolClass",fetch = FetchType.EAGER)
    List<User> users;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
