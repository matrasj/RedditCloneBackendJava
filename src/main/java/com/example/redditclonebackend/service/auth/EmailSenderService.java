package com.example.redditclonebackend.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Async
    void sendConfirmationEmail(String token, String targetEmail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setSubject("Account activation");
        simpleMailMessage.setFrom("reddit.backend@gmail.com");
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setText("Thank you for singing up to RedditClone"
                + "\nClick on url below to confirm your account: "  + "http://localhost:8081/api/v1/auth/confirm?token=" + token);

        javaMailSender.send(simpleMailMessage);
    }

    @Async
    void sendRetrievingPasswordEmail(String token, String targetEmail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setSubject("Password retrieving");
        simpleMailMessage.setFrom("reddit.backend@gmail.com");
        simpleMailMessage.setText("Hi!\n"
                + "\nClick on url below to change your password: " + "http://localhost:8081/api/v1/auth/retrieve/password?token=" + token);

        javaMailSender.send(simpleMailMessage);
    }


}
