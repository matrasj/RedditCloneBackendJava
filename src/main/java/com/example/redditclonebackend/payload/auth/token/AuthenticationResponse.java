package com.example.redditclonebackend.payload.auth.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
    private Date expiresAt;
    private String username;
}
