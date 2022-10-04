package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.payload.user.UserSearchPayload;

public class UserSearchPayloadMapper {
    public static UserSearchPayload mapToUserSearchPayload(User user) {
        return new UserSearchPayload(
                user.getUsername(),
                user.getProfileImageLink()
                );
    }
}
