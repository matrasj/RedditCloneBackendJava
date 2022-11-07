package com.example.redditclonebackend.payload.auth.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username can not be null")
    @Size(min = 4, max = 25, message = "Username must be longer than 4 and less than 25 characters")
    private String username;

    @NotBlank(message = "Password can not be null")
    @Size(min = 4, max = 25, message = "Password must be longer than 4 and less than 25 characters")
    private String password;
}
