package com.example.OnlineExam.dto;

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