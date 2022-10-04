package com.example.redditclonebackend.service.auth;

import com.example.redditclonebackend.entity.ConfirmationToken;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.exception.*;
import com.example.redditclonebackend.exception.registration.EmailAlreadyExistsException;
import com.example.redditclonebackend.exception.registration.UsernameAlreadyExistsException;
import com.example.redditclonebackend.payload.auth.login.LoginRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationResponse;
import com.example.redditclonebackend.payload.auth.token.AuthenticationResponse;
import com.example.redditclonebackend.payload.auth.token.RefreshTokenRequest;
import com.example.redditclonebackend.repository.ConfirmationTokenRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.security.JWTProvider;
import com.example.redditclonebackend.service.RefreshTokenService;
import com.example.redditclonebackend.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private static final String EMAIL_INVALID_MESSAGE = "Email %s is not valid";
    private static final String USERNAME_INVALID_MESSAGE = "Username %s is not valid";
    private static final String PASSWORD_INVALID_MESSAGE = "Password %s is not valid";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with email %s already exists";
    private static final boolean DISABLED_ACCOUNT = false;
    private static final String TOKEN_NOT_EXISTS_MESSAGE = "Token %s does not exists";
    private static final String TOKEN_ALREADY_CONFIRMED_MESSAGE = "Token %s has been already confirmed";
    private static final String TOKEN_ALREADY_EXPIRED_MESSAGE = "Token %s has already expired";
    private static final boolean ENABLE_ACCOUNT = true;
    private static final String SUCCESSFULLY_CONFIRMED_TOKEN_MESSAGE = "Successful token confirmation";
    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username %s already exists";

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenFactory confirmationTokenFactory;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;



    @Transactional
    public RegistrationResponse registerUser(RegistrationRequest request) {
        if (!ValidationService.validateEmail(request.getEmail())) {
            log.error("Invalid email --> " + request.getEmail());
            throw new EmailNotValidException(String.format(EMAIL_INVALID_MESSAGE, request.getEmail()));
        }

        if (!ValidationService.validateUsername(request.getUsername())) {
            log.error("Invalid username --> " + request.getUsername());
            throw new UsernameNotValidException(String.format(USERNAME_INVALID_MESSAGE, request.getUsername()));
        }

        if (!ValidationService.validatePassword(request.getPassword())) {
            log.error("Invalid password --> " + request.getPassword());
            throw new PasswordNotValidException(String.format(PASSWORD_INVALID_MESSAGE, request.getPassword()));
        }

        boolean suchEmailAlreadyExists = userRepository.findByEmail(request.getEmail()).isPresent();

        if (suchEmailAlreadyExists) {
            log.error(String.format("User with email %s already exists", request.getEmail()));
            throw new EmailAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, request.getEmail()));
        }

        boolean suchUsernameAlreadyExists = userRepository.findByUsername(request.getUsername()).isPresent();

        if (suchUsernameAlreadyExists) {
            log.error(String.format("User with username %s already exists", request.getUsername()));
            throw new UsernameAlreadyExistsException(String.format(USERNAME_ALREADY_EXISTS_MESSAGE, request.getUsername()));
        }

        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(DISABLED_ACCOUNT)
                .build();

        ConfirmationToken confirmationToken = confirmationTokenFactory.generateConfirmationToken();
        confirmationToken.setUser(user);

        log.info("Saving user: " + user);
        userRepository.save(user);
        confirmationTokenRepository.save(confirmationToken);

        emailSenderService.sendConfirmationEmail(confirmationToken.getToken(), user.getEmail());

        return new RegistrationResponse(confirmationToken);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotExistsException(String.format(TOKEN_NOT_EXISTS_MESSAGE, token)));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenAlreadyExpiredException(String.format(TOKEN_ALREADY_CONFIRMED_MESSAGE, token));
        }

        if (confirmationToken.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new TokenAlreadyExpiredException(String.format(TOKEN_ALREADY_EXPIRED_MESSAGE, token));
        }

        confirmationToken.getUser().setEnabled(ENABLE_ACCOUNT);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        return SUCCESSFULLY_CONFIRMED_TOKEN_MESSAGE;
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())))
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s has not been found", principal.getSubject())));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Date.from(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
