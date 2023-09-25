package com.example.OnlineExam.model.test;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private int questionId;
    @ManyToOne()
    @JoinColumn(name = "test_id")
    private Test test;
    @Column(name = "question")
    private String question;


}
