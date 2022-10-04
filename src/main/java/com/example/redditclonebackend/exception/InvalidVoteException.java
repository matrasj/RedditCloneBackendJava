package com.example.redditclonebackend.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidVoteException extends RuntimeException{
    public InvalidVoteException(String message) {
        super(message);
    }
}
