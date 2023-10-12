package com.example.OnlineExam.controller;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.GradeRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class GradeRepoTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    TestRepository testRepository;
    @Test
    public void addGrade(){
        //given
        insertUsers();
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        test.setTestCreator(user.getUsername());
        test.setTestName("testName");
        testRepository.save(test);

        Grade grade = new Grade();
        LocalDateTime now = LocalDateTime.now();
        grade.setTest(test);
        grade.setGrade(5);
        grade.setUser(user);
        //when
        gradeRepository.save(grade);
        //then
        assertEquals(test,grade.getTest());
        assertEquals(user,grade.getUser());
        assertEquals(5,grade.getGrade());
        assertEquals(now,grade.getDateTime());
    }
    @Test
    public void nullGradeShouldThrowException(){
        //given
        Grade grade = new Grade();
        grade.setGrade(5);
        //when
        assertThrows(ConstraintViolationException.class,() -> gradeRepository.save(grade));
    }

    private void insertUsers(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userService.addUserRole("test1","TEACHER");




    }


}
