package com.example.OnlineExam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class DuplicateTestException extends RuntimeException {
    public DuplicateTestException() {
        super("Test already exists");
    }
}