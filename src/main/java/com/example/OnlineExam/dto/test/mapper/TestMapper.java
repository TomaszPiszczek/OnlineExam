package com.example.OnlineExam.dto.test.mapper;

import com.example.OnlineExam.dto.test.AnswerDTO;
import com.example.OnlineExam.dto.test.QuestionDTO;
import com.example.OnlineExam.dto.test.TestDTO;
import com.example.OnlineExam.model.test.Answer;
import com.example.OnlineExam.model.test.Question;
import com.example.OnlineExam.model.test.Test;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TestMapper {

    public TestDTO mapToTestDTO(Test test,Integer score,Boolean finished) {
        List<QuestionDTO> questionDTOs = test.getQuestions()
                .stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList());

        return new TestDTO(
                test.getId(),
                test.getTestName(),
                test.getTestCreator(),
                test.getDateTime(),
                test.getSubject().getSubjectName(),
                test.getExpireDate(),
                score,
                questionDTOs,
                finished

        );
    }

    private QuestionDTO mapToQuestionDTO(Question question) {
        List<AnswerDTO> answerDTOs = question.getAnswers()
                .stream()
                .map(this::mapToAnswerDTO)
                .collect(Collectors.toList());

        return new QuestionDTO(
                question.getQuestion(),
                answerDTOs,
                question.getPoints()
        );
    }

    private AnswerDTO mapToAnswerDTO(Answer answer) {
        return new AnswerDTO(
                answer.getAnswer(),
                answer.isCorrect()
        );
    }
}