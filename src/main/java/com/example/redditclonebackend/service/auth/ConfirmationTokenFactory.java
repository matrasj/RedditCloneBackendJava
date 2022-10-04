package com.example.redditclonebackend.service.auth;

import com.example.redditclonebackend.entity.ConfirmationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenFactory {
    public ConfirmationToken generateConfirmationToken() {
        return ConfirmationToken.builder()
                .token(generateRandomToken())
                .createdAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .build();
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }
}
