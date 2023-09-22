package com.example.OnlineExam.model.subject;

import com.example.OnlineExam.model.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int id;
    @Column(name = "subject")
    private String subjectName;
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "subjects")
    private Set<User> users;

    public String getSubjectName() {
        return subjectName;
    }

    public Set<User> getUsers() {
        if(users == null){
            users = new HashSet<>();
        }
        return users;
    }


    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }



}
