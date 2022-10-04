package com.example.redditclonebackend.payload.socialmedialink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SocialMediaLinkPayloadResponse {
    private Long id;
    private String url;
    private String socialMediaName;
}
