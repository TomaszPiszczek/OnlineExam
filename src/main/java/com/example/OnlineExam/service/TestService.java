package com.example.OnlineExam.service;

import com.example.OnlineExam.dto.TestDTO;
import com.example.OnlineExam.dto.mapper.TestMapper;
import com.example.OnlineExam.exception.*;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.test.AnswerRepository;
import com.example.OnlineExam.repository.test.QuestionRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
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
    TestRepository testRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    TestMapper testMapper;
    SchoolClassRepository schoolClassRepository;
    public TestService(TestRepository testRepository, UserRepository userRepository, SubjectRepository subjectRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, TestMapper testMapper, SchoolClassRepository schoolClassRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.testMapper = testMapper;
        this.schoolClassRepository = schoolClassRepository;
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
                log.warn(user.getUsername() + "Inside add user to test");

                user.addTest(test);

                user.getTests().forEach(test1 -> log.warn(test1.getTestName() + "user"));
                test.getUsers().forEach(user1 -> log.warn(user1.getUsername() + "test"));
            }
        }
    }

    @Transactional
    public void changeTestName(int testId,String name){
        Test test = testRepository.getTestById(testId).orElseThrow(TestNotFoundException::new);
        test.setTestName(name);
    }
    public List<TestDTO> getTests(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        List<TestDTO> tests = new ArrayList<>();
        List<Test> test;
        if(user.getRoles().stream().anyMatch(role -> role.getAuthority().contains("ROLE_TEACHER"))){

            test =  testRepository.getTestsByTestCreator(userName).orElseThrow(TestNotFoundException::new);
            test.forEach(test1 -> tests.add(testMapper.mapToTestDTO(test1)));
            return tests;
        }

        test=testRepository.getTestsByUserName(userName).orElseThrow(TestNotFoundException::new);
        test.forEach(test1 -> tests.add(testMapper.mapToTestDTO(test1)));
        return tests;
    }
    //todo testsForThisMethod
    @Transactional
    public void addTestToClass(String className,Integer testId){
        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName(className).orElseThrow(SchoolClassNotFoundException::new);
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