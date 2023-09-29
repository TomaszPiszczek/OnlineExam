package com.example.OnlineExam.repository.subject;

import com.example.OnlineExam.model.subject.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
}
