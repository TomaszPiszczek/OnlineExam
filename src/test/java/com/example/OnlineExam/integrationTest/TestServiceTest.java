package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.exception.*;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Answer;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.test.AnswerRepository;
import com.example.OnlineExam.repository.test.QuestionRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.SchoolClassService;
import com.example.OnlineExam.service.SubjectService;
import com.example.OnlineExam.service.test.TestService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestServiceTest {
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectService subjectService;
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
    @Autowired
    private SchoolClassService schoolClassService;


    private Question question;
    private Answer answer;
    private Subject subject;
    private Integer testId;

    @BeforeEach
    public void setUp(){
        this.answer = new Answer();
        this.answer.setAnswer("answer");
        answerRepository.save(answer);

        this.question = new Question();
        this.question.setQuestion("question");
        this.question.setPoints(2);
        this.question.setAnswers(new HashSet<>(Collections.singletonList(answer)));
        questionRepository.save(question);

        this.subject = new Subject();
        this.subject.setSubjectName("math");
        subjectService.createSubject(subject);



    }

    @Test
    public  void connectionTest(){
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void testThrowUserNotFoundExceptionWithWrongUsername() {

        //given

        Subject subject = new Subject();
        subject.setSubjectName("math");
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();


        //then
        assertThrows(UsernameNotFoundException.class,() -> testService.createTest(test));

    }
    @Test
    public void testThrowQuestionNotFoundExceptionWithNullQuestion() {

        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        test.setTestCreator("test");
        test.setSubject(subject);


        //then
        assertThrows(QuestionNotFoundException.class,() -> testService.createTest(test));
    }
    @Test
    public void testThrowAnswerNotFoundExceptionWithNullQuestion() {

        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        test.setTestCreator("test");
        test.setSubject(subject);
        question.setAnswers(null);
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setTestName("testName");

        //then
        assertThrows(AnswerNotFoundException.class,() -> testService.createTest(test));
    }

    @Test
    public void testThrowSubjectNotFoundExceptionWithWrongSubjectIdJSON() throws Exception {
        //given

        Subject subject = new Subject();
        subject.setSubjectName("math");

        String json = "{\"testName\":\"Test Math1\",\"testCreator\":\"test\",\"subject\":{\"id\":" + (subject.getId()+100) + "},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}],\"points\": 3},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}],\"points\": 3}]}";
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void getTestsShouldReturnTests(){
        //given
        insertUsers();
        User user = userRepository.getUserByUsername("test").orElseThrow();

        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        com.example.OnlineExam.model.test.Test test1 = new com.example.OnlineExam.model.test.Test();

        test.setTestName("test");
        test.setTestCreator(user.getUsername());
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setSubject(subject);

        test1.setTestName("test2");
        test1.setTestCreator(user.getUsername());
        test1.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test1.setSubject(subject);

        //when

        testService.createTest(test);
        testService.createTest(test1);
        List<com.example.OnlineExam.model.test.Test> tests = testRepository.getTestsByTestCreator("test").orElseThrow();
        //then
        assertEquals(2,tests.size());
    }

    @Test
    public void createDuplicateTestShouldThrowException(){
        //given
        insertUsers();
        User user = userRepository.getUserByUsername("test").orElseThrow();

        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        com.example.OnlineExam.model.test.Test test1 = new com.example.OnlineExam.model.test.Test();

        test.setTestName("test");
        test.setTestCreator(user.getUsername());
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setSubject(subject);

        test1.setTestName("test");
        test1.setTestCreator(user.getUsername());
        test1.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test1.setSubject(subject);

        //when
        testService.createTest(test);

        //then
        assertThrows(DuplicateTestException.class,() -> testService.createTest(test1));
    }

    @Test
    public void addUsersToTest(){
        //given
        insertUsers();
        createTest();
        //when
        testService.addUsersToTest(Set.of("student","student1"),testId);
        com.example.OnlineExam.model.test.Test test = testRepository.getTestById(testId).orElseThrow(TestNotFoundException::new);

        //then
        assertEquals(2,test.getUsers().size());
    }
    @Test
    public void addTestToClassShouldAddEveryStudentFromClassToTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        insertUsers();
        createTest();
        addUsersToClass();
        com.example.OnlineExam.model.test.Test test = testRepository.getTestByTestName("testName").orElseThrow(TestNotFoundException::new);
        //when
        assertEquals(0,test.getUsers().size());
        testService.addTestToClass("4B",test.getId());
        //then
        assertEquals(2,test.getUsers().size());
    }
    @Test
    public void removeUsersFromTest(){
        //given
        insertUsers();
        createTest();
        //when
        testService.addUsersToTest(Set.of("student","student1"),testId);
        com.example.OnlineExam.model.test.Test test = testRepository.getTestById(testId).orElseThrow();

        testService.removeUsersFromTest("student","testName");
        //then
        assertEquals(1,test.getUsers().size());
    }


    private void insertUsers() {

        userRepository.save(new User("test", "password", true, "name", "surname", "email"));
        userRepository.save(new User("student", "password", true, "name", "surname", "email"));
        userRepository.save(new User("student1", "password", true, "name", "surname", "email"));
    }

    private void createTest(){
        com.example.OnlineExam.model.test.Test test = new com.example.OnlineExam.model.test.Test();
        test.setTestCreator("test");
        test.setSubject(subject);
        test.setQuestions(new HashSet<>(Collections.singletonList(question)));
        test.setTestName("testName");
        testRepository.save(test);
        testId = test.getId();
    }

    private void addUsersToClass() throws NoSuchFieldException, IllegalAccessException {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4B");
        Field usersField = SchoolClass.class.getDeclaredField("users");
        usersField.setAccessible(true);
        Set<User> newUsers = new HashSet<>();
        newUsers.add(userRepository.getUserByUsername("student").orElseThrow());
        newUsers.add(userRepository.getUserByUsername("student1").orElseThrow());
        Set<User> currentUsers = new HashSet<>(newUsers);
        usersField.set(schoolClass, currentUsers);
        schoolClassService.createClass(schoolClass);
    }



}
