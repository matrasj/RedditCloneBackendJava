package com.example.redditclonebackend.validation;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class UsernameValidator implements Predicate<String> {
    @Override
    public boolean test(String username) {
        return username.length() > 4;
    }
}
