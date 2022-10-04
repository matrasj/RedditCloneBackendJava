package com.example.redditclonebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class UsernameNotValidException extends RuntimeException {
    public UsernameNotValidException(String message) {
        super(message);
    }
}
