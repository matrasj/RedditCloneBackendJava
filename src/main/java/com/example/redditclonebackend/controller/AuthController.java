package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.auth.login.LoginRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationResponse;
import com.example.redditclonebackend.payload.auth.login.AuthenticationResponse;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> signUp(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.status(CREATED)
                .body(authService.registerUser(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam String token) {
        return ResponseEntity.status(ACCEPTED)
                .body(authService.confirmToken(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(OK)
                .body(authService.login(loginRequest));
    }






}
