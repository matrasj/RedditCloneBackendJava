package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.mapper.UserPayloadMapper;
import com.example.redditclonebackend.mapper.UserPayloadResponseMapper;
import com.example.redditclonebackend.mapper.UserSearchPayloadMapper;
import com.example.redditclonebackend.payload.user.UserPayload;
import com.example.redditclonebackend.payload.user.UserPayloadResponse;
import com.example.redditclonebackend.payload.user.UserSearchPayload;
import com.example.redditclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String USERNAME_NOT_FOUND_MESSAGE = "Not found user with username: %s";

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(getAuthorities("USER"))
                .build();

    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
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
