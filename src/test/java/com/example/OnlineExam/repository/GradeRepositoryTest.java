package com.example.OnlineExam.repository;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.GradeRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.GradeService;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GradeRepositoryTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    GradeService gradeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GradeRepository gradeRepository;

    @BeforeEach
    void setUp(){
        User user = new User();
        userRepository.save(user);

        Grade grade = new Grade();
        grade.setGrade(3);
        grade.setUser(user);

        Grade grade1 = new Grade();
        grade1.setGrade(4);
        grade1.setUser(user);

        gradeRepository.save(grade1);
        gradeRepository.save(grade);
    }

    @Test
    public void getGradesForUserShouldReturnGrades(){

        assertEquals(2,gradeRepository.getGradeByUserUserId(1).size());
    }


}
