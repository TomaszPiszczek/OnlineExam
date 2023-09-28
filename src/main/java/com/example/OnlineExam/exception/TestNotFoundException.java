package com.example.OnlineExam.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class TestNotFoundException extends RuntimeException {
    public TestNotFoundException() {
        super("Test not found");
    }
}