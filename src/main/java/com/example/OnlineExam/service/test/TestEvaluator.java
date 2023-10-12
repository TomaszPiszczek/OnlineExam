package com.example.OnlineExam.service.test;

import com.example.OnlineExam.dto.AnswerDTO;
import com.example.OnlineExam.dto.mapper.TestMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
@Slf4j
@Component
public class TestEvaluator {

    private TestMapper testMapper;
    private TestRepository testRepository;
    private UserRepository userRepository;
    private StudentTestRepository studentTestRepository;

    public TestEvaluator(TestMapper testMapper, TestRepository testRepository, UserRepository userRepository, StudentTestRepository studentTestRepository) {
        this.testMapper = testMapper;
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.studentTestRepository = studentTestRepository;
    }


    //todo test this method
    /**
     *
     * @return
     * Return the percentage value of correct test answers.
     */
    //todo Test id jest po to zeby dodac wynik do tabli student_test, userName rowniez
    //todo RateTest działa tylko wtedy kiedy egzamin nie wygasl
    public int RateTest(List<Question> questions, String userName, Integer testId){
        double maxScore =0;
        double score =0;

        maxScore = questions.stream().mapToDouble(question -> question.getPoints()).sum();

        score =questions.stream()
                .filter(question -> question.getAnswers().stream()
                        .allMatch(Answer::isCorrect))
                .mapToDouble(Question::getPoints)
                .sum();

        log.warn("score" + score);

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
        studentTestRepository.save(studentTest);
    }
    private Question findQuestionForAnswer(Set<Question> questions, AnswerDTO answer) {
        return questions.stream()
                .filter(question -> question.getAnswers().contains(answer))
                .findFirst()
                .orElse(null);
    }


}
