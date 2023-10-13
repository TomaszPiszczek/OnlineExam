package com.example.OnlineExam.dto.test;

import com.example.OnlineExam.dto.test.AnswerDTO;

import java.util.List;

public record QuestionDTO(
        String question,
        List<AnswerDTO> answers,
        Integer points
) {
    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public int getPoints() {
        return points;
    }
}