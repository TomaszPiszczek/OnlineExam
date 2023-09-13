package com.example.OnlineExam.model.test;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private  int answerId;
    @Column(name = "question_id")
    private int question_Id;
    @Column(name = "answer")
    private String answer;
    @Column(name = "correct")
    private boolean correct;

    @ManyToOne()
    @JoinColumn(name = "question_id")
    private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer(int question_Id, String answer, boolean correct) {
        this.question_Id = question_Id;
        this.answer = answer;
        this.correct = correct;
    }

    public int getQuestion_Id() {
        return question_Id;
    }

    public void setQuestion_Id(int question_Id) {
        this.question_Id = question_Id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getAnswerId() {
        return answerId;
    }
}
