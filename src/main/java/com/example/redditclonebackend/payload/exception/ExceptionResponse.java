package com.example.redditclonebackend.payload.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionResponse {
    private String message;
    private HttpStatus httpStatus;
}
