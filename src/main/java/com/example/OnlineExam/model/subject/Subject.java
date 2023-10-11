package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int id;
    @Column(name = "subject",unique = true)
    @NotNull(message = "Subject name cannot be blank")
    @Length(min = 2, max = 50, message = "Subject name must be between 2-50 characters")
    private String subjectName;
    @ManyToMany(mappedBy = "subjects")
    private Set<User> users;
    @OneToMany(mappedBy = "subject")
    private List<Test> tests;


    public String getSubjectName() {
        return subjectName;
    }

    public Set<User> getUsers() {
        if(users == null){
            users = new HashSet<>();
        }
        return users;
    }

    public int getId() {
        return id;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }




}
