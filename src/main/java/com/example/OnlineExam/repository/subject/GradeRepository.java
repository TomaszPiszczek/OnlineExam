package com.example.OnlineExam.repository.subject;

import com.example.OnlineExam.model.subject.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {
     List<Grade> getGradeByUserUserId(Integer id);
}
