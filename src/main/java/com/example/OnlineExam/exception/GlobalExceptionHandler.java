package com.example.OnlineExam.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorObject> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setTimestamp(new Date());

        //GET MESSAGE FROM ConstraintViolationException
        String message = ex.getMessage();
        int startIndex = message.indexOf("interpolatedMessage=");
        if (startIndex != -1) {
            startIndex += "interpolatedMessage=".length();
            int endIndex = message.indexOf(',', startIndex);

            if (endIndex != -1) {
                String extractedMessage = message.substring(startIndex, endIndex).trim();
                errorObject.setMessage(extractedMessage);
            }

        }
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_FOUND);

    }
}
