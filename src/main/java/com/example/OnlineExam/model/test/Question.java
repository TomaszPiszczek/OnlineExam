package com.example.OnlineExam.model.test;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int questionId;
 /*  @Column(name = "test_id")
    private int test_id;*/
    @Column(name = "question")
    private String question;
    @Column(name = "points")
    private int points;
    @ManyToOne()
    @JoinColumn(name = "test_id")
    private Test test;
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    List<Answer> answers;


    public Question(int test_id, String question, int points) {
       // this.test_id = test_id;
        this.question = question;
        this.points = points;
    }

   // public int getTest_id() {
    //    return test_id;
    //}

   /* public void setTest_id(int test_id) {
        this.test_id = test_id;
    }*/

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getQuestionId() {
        return questionId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
    public void addAnswer(Answer answer){
        if(this.answers == null){
            answers = new ArrayList<>();
        }
        answers.add(answer);
        answer.setQuestion(this);
    }

}
