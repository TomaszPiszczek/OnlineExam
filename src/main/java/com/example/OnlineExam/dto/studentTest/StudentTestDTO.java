package com.example.OnlineExam.dto.studentTest;

import jakarta.persistence.Column;

public record StudentTestDTO(@Column(name = "username")String userName,@Column(name = "testname") String testName, @Column(name = "test_result") Integer score) {
}