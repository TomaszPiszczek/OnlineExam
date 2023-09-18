package com.example.OnlineExam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException() {
        super("Username is already taken");
    }
}
