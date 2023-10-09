package com.example.OnlineExam.service;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    TestRepository testRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    public TestService(AnswerRepository answerRepository,QuestionRepository questionRepository,TestRepository testRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public void createTest(Test test){
        if(userRepository.getUserByUsername(test.getTestCreator()).isEmpty()) throw new UsernameNotFoundException();
        if(test.getSubject() == null) throw new SubjectNotFoundException();
        if(test.getQuestions() == null) throw new QuestionNotFoundException();
        if(test.getQuestions().stream().anyMatch(answer -> answer.getAnswers() == null )) throw new AnswerNotFoundException();
        if(getTests(test.getTestCreator()).stream().anyMatch(test1 -> test1.getTestName().equals(test.getTestName())))
        {
            throw new DuplicateTestException();
        }else {
            User user = userRepository.getUserByUsername(test.getTestCreator()).orElseThrow(UsernameNotFoundException::new);
            Subject subject = subjectRepository.getSubjectById(test.getSubject().getId()).orElseThrow(SubjectNotFoundException::new);
            Test saveTest = new Test();

            saveTest.setTestName(test.getTestName());
            test.setTestCreator(user.getUsername());
            test.setSubject(subject);
            testRepository.save(test);

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

    }
    @Transactional
    public void changeTestName(int testId,String name){
        Test test = testRepository.getTestById(testId).orElseThrow(TestNotFoundException::new);
        test.setTestName(name);
    }
    public List<Test> getTests(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        return testRepository.getTestsByUserName(user.getUsername()).orElseThrow(TestNotFoundException::new);
    }

}