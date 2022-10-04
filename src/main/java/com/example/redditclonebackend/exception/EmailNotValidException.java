package com.example.redditclonebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(BAD_REQUEST)
public class EmailNotValidException extends RuntimeException{
    public EmailNotValidException(String message) {
        super(message);
    }
}

