package com.example.redditclonebackend.payload.user;

import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPayloadResponse {
    private String email;
    private String username;
    private int postsNumber;
    private int commentsNumber;
    private String profileImageLink;
    private String biography;
    private List<SocialMediaLinkPayloadResponse> socialMediaLinks = new ArrayList<>();
}
