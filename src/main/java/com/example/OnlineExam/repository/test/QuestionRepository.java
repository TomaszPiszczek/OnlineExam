package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
}
