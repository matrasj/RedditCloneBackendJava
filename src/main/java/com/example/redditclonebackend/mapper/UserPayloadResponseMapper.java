package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.payload.user.UserPayloadResponse;

import java.util.stream.Collectors;

public class UserPayloadResponseMapper {
    public static UserPayloadResponse mapToUserPayloadResponse(User user) {
        return UserPayloadResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .postsNumber(user.getPosts().size())
                .commentsNumber(user.getComments().size())
                .profileImageLink(user.getProfileImageLink())
                .biography(user.getBiography())
                .socialMediaLinks(user.getLinks()
                        .stream()
                        .map(SocialMediaLinkPayloadMapper::mapToSocialMediaLinkPayloadResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
