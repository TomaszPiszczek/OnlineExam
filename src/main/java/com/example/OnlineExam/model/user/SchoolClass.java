package com.example.OnlineExam.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class")
    @NotBlank(message = "Class name cannot be blank")
    @Length(min = 2, max = 50, message = "Class name must be between 2-50 characters")
    private String name;
    @OneToMany(mappedBy = "schoolClass")
    private List<User> users;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }
}
