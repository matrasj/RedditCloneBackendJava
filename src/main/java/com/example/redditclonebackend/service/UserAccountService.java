package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private static final String SUCCESSFULLY_DELETED_BIOGRAPHY = "Successfully deleted biography!";
    private final AuthService authService;
    private final UserRepository userRepository;
    private static final String SUCCESSFULLY_SET_BIOGRAPHY = "Successfully set biography!";
    public String setUserBiography(String biography) {
        User currentUser = authService.getCurrentUser();
        currentUser.setBiography(biography);

        userRepository.save(currentUser);

        return SUCCESSFULLY_SET_BIOGRAPHY;
    }

    public String deleteBiography() {
        User currentUser = authService.getCurrentUser();
        currentUser.setBiography(null);
        userRepository.save(currentUser);

        return SUCCESSFULLY_DELETED_BIOGRAPHY;
    }
}
