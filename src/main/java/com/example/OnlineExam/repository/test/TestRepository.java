package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test,Integer> {
    Optional<Test> getTestById(int testId);
}
