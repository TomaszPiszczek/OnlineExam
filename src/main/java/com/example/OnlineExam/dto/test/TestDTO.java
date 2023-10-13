package com.example.OnlineExam.dto.test;

import com.example.OnlineExam.dto.test.QuestionDTO;

import java.time.LocalDateTime;
import java.util.List;

public record TestDTO(
        int id,
        String testName,
        String testCreator,
        LocalDateTime dateTime,
        String subject,
        LocalDateTime expireDate,
        Integer score,
        List<QuestionDTO> questions
) {

}