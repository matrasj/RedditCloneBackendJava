package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.mapper.UserPayloadMapper;
import com.example.redditclonebackend.mapper.UserPayloadResponseMapper;
import com.example.redditclonebackend.mapper.UserSearchPayloadMapper;
import com.example.redditclonebackend.payload.user.UserPayload;
import com.example.redditclonebackend.payload.user.UserPayloadResponse;
import com.example.redditclonebackend.payload.user.UserSearchPayload;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private static final String SUCCESSFULLY_DELETED_BIOGRAPHY = "Successfully deleted biography!";
    private static final String USERNAME_NOT_FOUND_MESSAGE = "Not found user with username: %s";
    private static final String SUCCESSFULLY_SET_BIOGRAPHY = "Successfully set biography!";
    private final AuthService authService;
    private final UserRepository userRepository;

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

    @Transactional
    public Page<UserPayload> getUsersPage(int pageNumber, int pageSize) {
        List<UserPayload> mappedUsers = userRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(UserPayloadMapper::mapToUserPayload)
                .toList();

        return new PageImpl<>(
                mappedUsers,
                PageRequest.of(pageNumber, pageSize),
                mappedUsers.size()
        );
    }


    public UserPayloadResponse findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username)));

        return UserPayloadResponseMapper.mapToUserPayloadResponse(user);
    }

    public List<UserSearchPayload> findSearchedUsers(String username) {
        List<UserSearchPayload> searchedUsersPayload = userRepository.findByUsernameContaining(username)
                .stream()
                .map(UserSearchPayloadMapper::mapToUserSearchPayload)
                .toList();


        return searchedUsersPayload.size() <= 10
                ? searchedUsersPayload.subList(0, searchedUsersPayload.size()) : searchedUsersPayload.subList(0, 10);
    }
}
