package com.example.redditclonebackend.exception.registration;

import com.example.redditclonebackend.payload.exception.ExceptionResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class EmailAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse emailAlreadyExistsHandler(EmailAlreadyExistsException exception) {
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(BAD_REQUEST)
                .build();
    }
}
