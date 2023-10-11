package com.example.OnlineExam.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TestDTO(
                      String testName,
                      String testCreator,
                      LocalDateTime dateTime,
                      List<QuestionDTO> questions
) {
}