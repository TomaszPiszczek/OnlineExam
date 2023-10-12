package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentTestRepository extends JpaRepository<StudentTest,Integer> {
}
