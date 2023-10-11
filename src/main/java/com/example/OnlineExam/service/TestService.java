package com.example.OnlineExam.service;

import com.example.OnlineExam.dto.TestDTO;
import com.example.OnlineExam.dto.mapper.TestMapper;
import com.example.OnlineExam.exception.*;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.test.AnswerRepository;
import com.example.OnlineExam.repository.test.QuestionRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TestService {
    TestRepository testRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    TestMapper testMapper;

    public TestService(TestRepository testRepository, UserRepository userRepository, SubjectRepository subjectRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.testMapper = testMapper;
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
    public void addUsersToTest(Set<String> userNames, String testName){
        Test test = testRepository.getTestByTestName(testName).orElseThrow(TestNotFoundException::new);
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
    public List<TestDTO> getTests(String userName){
        if(userRepository.getUserByUsername(userName).isEmpty()) throw new UsernameNotFoundException();
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        if(user.getRoles().stream().anyMatch(role -> role.getAuthority().contains("ROLE_TEACHER"))){
            List<TestDTO> tests = new ArrayList<>();
            List<Test> test =  testRepository.getTestsByTestCreator(userName).orElseThrow(TestNotFoundException::new);

            test.forEach(test1 -> tests.add(testMapper.mapToTestDTO(test1)));

            //return testRepository.getTestsByTestCreator(userName).orElseThrow(TestNotFoundException::new);
            return tests;

        }
        return null;
       // return testRepository.getTestsByUserName(userName).orElseThrow(TestNotFoundException::new);
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