package com.example.OnlineExam.dto;

public record AnswerDTO(
        String answer,
        boolean correct
) {
    public boolean isCorrect() {
        return correct;
    }
}