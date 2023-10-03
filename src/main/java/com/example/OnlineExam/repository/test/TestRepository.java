package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test,Integer> {
    Optional<Test> getTestById(int testId);
    Optional<List<Test>> getTestsByUser(User user);

}
