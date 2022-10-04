package com.example.redditclonebackend.service;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

public class ValidationService {
        public static boolean validateEmail(String email) {
            return email.contains("@");
        }

        public static boolean validateUsername(String username) {
            return username.length() > 2;
        }

        public static boolean validatePassword(String password) {
            return password.length() > 2;
        }
}
