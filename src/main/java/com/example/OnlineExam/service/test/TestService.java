package com.example.OnlineExam.service.test;

import com.example.OnlineExam.dto.studentTest.StudentTestDTO;
import com.example.OnlineExam.dto.test.TestDTO;
import com.example.OnlineExam.dto.test.mapper.TestMapper;
import com.example.OnlineExam.exception.*;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.test.*;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class TestService {
    @PersistenceContext
    private EntityManager entityManager;
   private final   TestRepository testRepository;
   private final   UserRepository userRepository;
   private final   SubjectRepository subjectRepository;
   private final QuestionRepository questionRepository;
   private final AnswerRepository answerRepository;
   private final TestMapper testMapper;
   private final SchoolClassRepository schoolClassRepository;

    private final StudentTestRepository studentTestRepository;

    public TestService(TestRepository testRepository, UserRepository userRepository, SubjectRepository subjectRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, TestMapper testMapper, SchoolClassRepository schoolClassRepository, StudentTestRepository studentTestRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.testMapper = testMapper;
        this.schoolClassRepository = schoolClassRepository;
        this.studentTestRepository = studentTestRepository;
    }

    @Transactional
    public void createTest(@NotNull Test test){
        validateTest(test);

            User user = userRepository.getUserByUsername(test.getTestCreator()).orElseThrow(UsernameNotFoundException::new);
            Subject subject = subjectRepository.getSubjectById(test.getSubject().getId()).orElseThrow(SubjectNotFoundException::new);
            test.setTestCreator(user.getUsername());
            test.setSubject(subject);
            testRepository.save(test);
            addQuestionsAndAnswersToTest(test);
    }
    @Transactional
    public void removeUsersFromTest(String username,String testName){
        Test test = testRepository.getTestByTestName(testName).orElseThrow(TestNotFoundException::new);
        User user = userRepository.getUserByUsername(username).orElseThrow(UsernameNotFoundException::new);

        if(user.getTests().contains(test)){
            user.removeTest(test);
        }
    }
    @Transactional
    public void addUsersToTest(Set<String> userNames, int id){
        Test test = testRepository.getTestById(id).orElseThrow(TestNotFoundException::new);
        for (String name:userNames
        ) {
            User user = userRepository.getUserByUsername(name).orElseThrow(UsernameNotFoundException::new);
            if(!user.getTests().contains(test)){

                user.addTest(test);

            }
        }
    }

    @Transactional
    public void changeTestName(int testId,String name){
        Test test = testRepository.getTestById(testId).orElseThrow(TestNotFoundException::new);
        test.setTestName(name);
    }
    public List<TestDTO> getTestsByStudentName(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        List<TestDTO> tests = new ArrayList<>();
        List<Test> test;


        test=testRepository.getTestsByUserName(userName);
        test.forEach(test1 -> {
            //Integer score = studentTestRepository.findTestScoreByTestIdAndUserId(test1.getId(),user.getUserId());
            //Boolean finished = studentTestRepository.isFinishedTestByTestIdAndUserId(test1.getId(),user.getUserId());
            tests.add(testMapper.mapToTestDTO(test1,0,false));
        });
        return tests;
    }
    public List<TestDTO> getTestsByTestCreator(String testCreator){
        userRepository.getUserByUsername(testCreator).orElseThrow(UsernameNotFoundException::new);
        List<TestDTO> tests = new ArrayList<>();
        List<Test> test;

        test =  testRepository.getTestsByTestCreator(testCreator).orElseThrow(TestNotFoundException::new);
        test.forEach(test1 -> tests.add(testMapper.mapToTestDTO(test1,null,null)));
        return tests;
    }
    public List<TestDTO> getNotFinishedTest(String userName){
        return getTestsByStudentName(userName).stream().filter(testDTO -> !testDTO.finished()).collect(Collectors.toList());
    }
    public List<StudentTestDTO> getTestsForClass(String className,int testId) {
        if(!schoolClassRepository.existsByName(className)) throw new SchoolClassNotFoundException();
        if(testRepository.getTestById(testId).isEmpty()) throw new TestNotFoundException();
        String jpql = "SELECT NEW com.example.OnlineExam.dto.studentTest.StudentTestDTO(u.username, t.testName, st.testResult) " +
                "FROM StudentTest st " +
                "JOIN st.user u " +
                "JOIN st.test t " +
                "WHERE t.id = :testId " +
                "AND u.schoolClass.name = :className";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("testId", testId);
        query.setParameter("className", className);

        return query.getResultList();
    }
    @Transactional
    public void addTestToClass(String className,Integer testId){
        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName(className).orElseThrow(SchoolClassNotFoundException::new);
        if(schoolClass.getUsers() == null) throw new IllegalStateException("Class is empty");
        Set<String> users = schoolClass.getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        users.forEach(log::warn);
        addUsersToTest(users,testId);
    }
    private void addQuestionsAndAnswersToTest(Test test){
        test.getQuestions().forEach(
                question -> {
                    question.setTest(test);
                    questionRepository.save(question);
                    question.getAnswers().forEach(answer -> {
                        answer.setQuestion(question);
                        answerRepository.save(answer);
                    });
                });
    }
    private void validateTest(Test test) {
        if(userRepository.getUserByUsername(test.getTestCreator()).isEmpty()) throw new UsernameNotFoundException();
        if(test.getSubject() == null) throw new SubjectNotFoundException();
        if(test.getQuestions() == null) throw new QuestionNotFoundException();
        if(test.getQuestions().stream().anyMatch(answer -> answer.getAnswers() == null )) throw new AnswerNotFoundException();
        List<Test> tests = testRepository.getTestsByTestCreator(test.getTestCreator()).orElse(Collections.emptyList());
        if(tests.stream().anyMatch(test1 -> test1.getTestName().equals(test.getTestName()))) throw new DuplicateTestException();
    }



}