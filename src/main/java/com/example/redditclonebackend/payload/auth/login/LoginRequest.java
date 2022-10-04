package com.example.redditclonebackend.payload.auth.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
