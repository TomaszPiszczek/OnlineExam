package com.example.OnlineExam.dto;

import java.util.List;

public record QuestionDTO (
        String question,
        List<AnswerDTO> answers
) {
}