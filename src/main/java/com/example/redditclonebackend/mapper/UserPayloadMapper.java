package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.payload.user.UserPayload;

public class UserPayloadMapper {
    public static UserPayload mapToUserPayload(User user) {
        return UserPayload.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .lastUpdated(user.getLastUpdated())
                .build();
    }
}
