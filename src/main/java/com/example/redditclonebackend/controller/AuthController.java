package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.auth.login.LoginRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationResponse;
import com.example.redditclonebackend.payload.auth.token.AuthenticationResponse;
import com.example.redditclonebackend.payload.auth.token.RefreshTokenRequest;
import com.example.redditclonebackend.service.RefreshTokenService;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> signUp(@RequestBody RegistrationRequest request) {
        return ResponseEntity.status(CREATED)
                .body(authService.registerUser(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam String token) {
        return ResponseEntity.status(ACCEPTED)
                .body(authService.confirmToken(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(CREATED)
                .body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.status(OK)
                .body("Deleted successfully :-)");
    }


}
