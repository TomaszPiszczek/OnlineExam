package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.test.Answer;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.test.StudentTestRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.test.TestEvaluator;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@Transactional
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class,SpringExtension.class})
@SpringBootTest
public class TestEvaluatorTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Mock
    private TestRepository testRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentTestRepository studentTestRepository;
    TestEvaluator testEvaluator;
    @BeforeEach
    public void setUp(){
        testEvaluator = new TestEvaluator(testRepository,userRepository,studentTestRepository);
    }


    @Test
    public void testRateShouldReturnPercentageValueOfCorrectAnswers()
    {
        Optional<User> user = Optional.of(new User());
        Optional<com.example.OnlineExam.model.test.Test> test = Optional.of(new com.example.OnlineExam.model.test.Test());

        Answer answer = new Answer();
        answer.setCorrect(true);
        Question question =new Question();
        question.setPoints(3);
        question.setAnswers(Set.of(answer));


        Answer answer1 = new Answer();
        answer1.setCorrect(false);
        Question question1 =new Question();
        question1.setPoints(4);
        question1.setAnswers(Set.of(answer1));



        when(userRepository.getUserByUsername(Mockito.any(String.class))).thenReturn(user);
        when(testRepository.getTestById(Mockito.anyInt())).thenReturn(test);

        int score = testEvaluator.RateTest(List.of(question,question1), "userName", 1);

        assertEquals(43,score);
    }

}
