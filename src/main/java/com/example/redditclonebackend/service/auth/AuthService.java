package com.example.redditclonebackend.service.auth;

import com.example.redditclonebackend.entity.ConfirmationToken;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.UserPrincipal;
import com.example.redditclonebackend.exception.*;
import com.example.redditclonebackend.exception.registration.EmailAlreadyExistsException;
import com.example.redditclonebackend.exception.registration.UsernameAlreadyExistsException;
import com.example.redditclonebackend.factories.ConfirmationTokenFactory;
import com.example.redditclonebackend.jwt.JwtTokenProvider;
import com.example.redditclonebackend.payload.auth.login.LoginRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationRequest;
import com.example.redditclonebackend.payload.auth.registration.RegistrationResponse;
import com.example.redditclonebackend.payload.auth.login.AuthenticationResponse;
import com.example.redditclonebackend.repository.ConfirmationTokenRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.validation.EmailValidator;
import com.example.redditclonebackend.validation.PasswordValidator;
import com.example.redditclonebackend.validation.UsernameValidator;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;


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
    private static final String BAD_CREDENTIALS = "Bad credentials";

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenFactory confirmationTokenFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;



    @Transactional
    public RegistrationResponse registerUser(RegistrationRequest request) {
        validateData(request);

        ConfirmationToken confirmationToken = confirmationTokenFactory.generateConfirmationToken();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(DISABLED_ACCOUNT)
                .tokens(Sets.newHashSet(confirmationToken))
                .build();

        confirmationToken.setUser(user);
        userRepository.save(user);
        confirmationTokenRepository.save(confirmationToken);

        emailSenderService.sendConfirmationEmail(confirmationToken.getToken(), user.getEmail());

        return new RegistrationResponse(confirmationToken);
    }

    private void validateData(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail())) {
            throw new EmailNotValidException(String.format(EMAIL_INVALID_MESSAGE, request.getEmail()));
        }

        if (!usernameValidator.test(request.getUsername())) {
            throw new UsernameNotValidException(String.format(USERNAME_INVALID_MESSAGE, request.getUsername()));
        }

        if (!passwordValidator.test(request.getPassword())) {
            throw new PasswordNotValidException(String.format(PASSWORD_INVALID_MESSAGE, request.getPassword()));
        }


        boolean suchEmailAlreadyExists = userRepository.findByEmail(request.getEmail()).isPresent();

        if (suchEmailAlreadyExists) {
            throw new EmailAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, request.getEmail()));
        }

        boolean suchUsernameAlreadyExists = userRepository.findByUsername(request.getUsername()).isPresent();

        if (suchUsernameAlreadyExists) {
            throw new UsernameAlreadyExistsException(String.format(USERNAME_ALREADY_EXISTS_MESSAGE, request.getUsername()));
        }
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
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new AccessDeniedException(BAD_CREDENTIALS));

            String generatedJwtToken = jwtTokenProvider.generateJwtToken(authenticate);

            return AuthenticationResponse.builder()
                    .authenticationToken(generatedJwtToken)
                    .expiresAt(new Date(System.currentTimeMillis() + jwtTokenProvider.getValidityInMs()))
                    .username(user.getUsername())
                    .build();

        } catch (AuthenticationException authenticationException) {
            throw new AccessDeniedException(BAD_CREDENTIALS);
        }
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {

        Object principal = SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) {
            return null;
        } else {
            return userRepository.findByUsername(((UserPrincipal)principal).getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s has not been found", ((UserPrincipal)(principal)).getUsername())));
        }

    }

    public boolean isLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null;
    }
}
