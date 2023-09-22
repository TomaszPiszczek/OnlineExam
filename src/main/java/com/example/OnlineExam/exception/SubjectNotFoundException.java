package com.example.OnlineExam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException() {
        super("Subject not found");
    }
}
