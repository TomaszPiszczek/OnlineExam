package com.example.OnlineExam.model.test;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    @NotNull(message = "Question cannot be blank")
    private String question;
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers;


    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getQuestion() {
        return question;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
