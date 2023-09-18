package com.example.OnlineExam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super("The user with the provided username does not exist");
    }
}
