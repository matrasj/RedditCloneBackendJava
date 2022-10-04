package com.example.redditclonebackend.payload.auth.registration;

import com.example.redditclonebackend.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RegistrationResponse {
    private ConfirmationToken confirmationToken;
}
