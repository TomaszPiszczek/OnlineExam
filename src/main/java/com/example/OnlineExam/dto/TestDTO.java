package com.example.OnlineExam.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TestDTO(
        int id,
        String testName,
        String testCreator,
        LocalDateTime dateTime,
        String subject,

        List<QuestionDTO> questions
) {
}