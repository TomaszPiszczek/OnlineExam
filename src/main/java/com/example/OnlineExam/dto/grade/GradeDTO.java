package com.example.OnlineExam.dto.grade;

import java.time.LocalDateTime;

public record GradeDTO(String userName, Integer grade, String testName, LocalDateTime dateTime) {

}