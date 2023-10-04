package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.exception.DuplicateTestException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Answer;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.user.User;

import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.test.AnswerRepository;
import com.example.OnlineExam.repository.test.QuestionRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.TestService;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestServiceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    UserRepository userRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestService testService;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer answer;
    private Subject subject;

    @BeforeEach
    public void setUp(){
        this.answer = new Answer();
        this.answer.setAnswer("answer");

        this.question = new Question();
        this.question.setQuestion("question");
        this.question.setAnswers(new HashSet<>(Collections.singletonList(answer)));

        this.subject = new Subject();
        this.subject.setSubjectName("math");
        subjectRepository.save(subject);
        questionRepository.save(question);
        answerRepository.save(answer);
    }



    @Test
    public void testThrowUserNotFoundExceptionWithWrongUsernameJSON() throws Exception {

        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();


        //then
        assertThrows(UsernameNotFoundException.class,() -> testService.createTest(test));

    }
    @Test
    public void testThrowSubjectNotFoundExceptionWithWrongSubjectIdJSON() throws Exception {
        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);

        String json = "{\"testName\":\"Test Math2\",\"user\":{\"username\":\"test\"},\"subject\":{\"id\":1421},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}]},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}]}]}";
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Test
    public void createTestWithValidJSON() throws Exception {
        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);


        String json = "{\"testName\":\"Test Math1\",\"user\":{\"username\":\"test\"},\"subject\":{\"id\":" + subject.getId() + "},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}]},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}]}]}";
       //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void getTestsShouldReturnTests(){
        //given
        insertUsers();
        User user = userRepository.getUserByUsername("test").orElseThrow();

        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        com.example.OnlineExam.model.test.Test test1 = new com.example.OnlineExam.model.test.Test();

        test.setTestName("test");
        test.setUser(user);
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setSubject(subject);

        test1.setTestName("test2");
        test1.setUser(user);
        test1.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test1.setSubject(subject);

        //when

        testService.createTest(test);
        testService.createTest(test1);
        //then
        assertEquals(2,testService.getTests("test").size());
    }

    @Test
    public void createDuplicateTestShouldThrowException(){
        //given
        insertUsers();
        User user = userRepository.getUserByUsername("test").orElseThrow();

        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        com.example.OnlineExam.model.test.Test test1 = new com.example.OnlineExam.model.test.Test();

        test.setTestName("test");
        test.setUser(user);
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setSubject(subject);

        test1.setTestName("test");
        test1.setUser(user);
        test1.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test1.setSubject(subject);

        //when
        testService.createTest(test);

        //then
        assertThrows(DuplicateTestException.class,() -> testService.createTest(test1));
    }


    private void insertUsers() {
        subjectRepository.flush();

        userRepository.save(new User("test", "password", true, "name", "surname", "email"));

    }




}
