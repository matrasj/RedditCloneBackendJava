package com.example.redditclonebackend.payload.auth.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}
