package com.example.OnlineExam.dto;

import com.example.OnlineExam.model.subject.Subject;

import java.time.LocalDateTime;
import java.util.List;

public record TestDTO(
                      String testName,
                      String testCreator,
                      LocalDateTime dateTime,
                      String subject,

                      List<QuestionDTO> questions
) {
}