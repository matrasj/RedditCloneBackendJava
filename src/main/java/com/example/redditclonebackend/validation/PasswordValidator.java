package com.example.redditclonebackend.validation;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class PasswordValidator implements Predicate<String> {
    @Override
    public boolean test(String password) {
        return password.length() >= 4;
    }
}
