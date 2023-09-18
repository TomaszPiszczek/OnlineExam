package com.example.OnlineExam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SchoolClassNotFoundException extends RuntimeException{
    public SchoolClassNotFoundException() {

        super("Class not found");
    }
}
