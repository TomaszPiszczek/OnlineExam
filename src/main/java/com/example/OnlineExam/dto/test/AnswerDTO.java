package com.example.OnlineExam.dto.test;

public record AnswerDTO(
        String answer,
        boolean correct
) {
    public boolean isCorrect() {
        return correct;
    }
}