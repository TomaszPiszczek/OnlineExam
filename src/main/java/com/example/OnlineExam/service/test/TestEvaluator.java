package com.example.OnlineExam.service.test;

import com.example.OnlineExam.exception.TestNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.test.Answer;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.test.StudentTest;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.test.StudentTestRepository;
import com.example.OnlineExam.repository.test.TestRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class TestEvaluator {

    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final StudentTestRepository studentTestRepository;

    public TestEvaluator( TestRepository testRepository, UserRepository userRepository, StudentTestRepository studentTestRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.studentTestRepository = studentTestRepository;
    }


    //todo test this method
    /**
     *
     * @return
     * Return the percentage value of correct test answers,
     * and save it.
     */
    @Transactional
    public int RateTest(List<Question> questions, String userName, Integer testId){
        double maxScore;
        double score;
        maxScore = questions.stream().mapToDouble(Question::getPoints).sum();

        score =questions.stream()
                .filter(question -> question.getAnswers().stream()
                        .allMatch(Answer::isCorrect))
                .mapToDouble(Question::getPoints)
                .sum();

        int result = (int) Math.round((score / maxScore) * 100);
        saveTestResult(result,userName,testId);
        return result;
    }
    private void saveTestResult(Integer result,String userName,Integer testId){
        StudentTest studentTest = new StudentTest();
        Test test = testRepository.getTestById(testId).orElseThrow(TestNotFoundException::new);
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        studentTest.setTest(test);
        studentTest.setUser(user);
        studentTest.setTestResult(result);
        if( test.getExpireDate() == null) {
            studentTestRepository.save(studentTest);
            return;
        }
        if(test.getExpireDate().isAfter(LocalDateTime.now())){
            studentTestRepository.save(studentTest);
        }else throw new IllegalStateException("Test expired");

    }



}
