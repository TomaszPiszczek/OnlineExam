package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Integer> {
}
