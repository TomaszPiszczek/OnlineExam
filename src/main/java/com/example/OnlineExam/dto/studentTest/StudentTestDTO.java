package com.example.OnlineExam.dto.studentTest;

import jakarta.validation.constraints.NotNull;

public class StudentTestDTO {
    @NotNull(message = "Username cannot be blank")
    private String userName;

    @NotNull(message = "Test name cannot be null")
    private String testName;

    private Integer score;
    private Boolean finished;

    public StudentTestDTO(String userName, String testName, Integer score) {
        this.userName = userName;
        this.testName = testName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public String getTestName() {
        return testName;
    }

    public Integer getScore() {
        return score;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
