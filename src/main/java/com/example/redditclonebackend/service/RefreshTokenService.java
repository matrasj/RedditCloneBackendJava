package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.RefreshToken;
import com.example.redditclonebackend.exception.RefreshTokenNotFoundException;
import com.example.redditclonebackend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private static final String REFRESH_TOKEN_NOT_FOUND_MESSAGE = "Not found token --> %s";
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateRandomUUID())
                .createdAt(Date.from(Instant.now()))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public void validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(String.format(REFRESH_TOKEN_NOT_FOUND_MESSAGE, token)));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
