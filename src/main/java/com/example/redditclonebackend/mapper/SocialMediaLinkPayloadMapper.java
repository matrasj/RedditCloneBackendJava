package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.SocialMediaLink;
import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadResponse;

public class SocialMediaLinkPayloadMapper {
    public static SocialMediaLinkPayloadResponse mapToSocialMediaLinkPayloadResponse(SocialMediaLink socialMediaLink) {
        return SocialMediaLinkPayloadResponse.builder()
                .id(socialMediaLink.getId())
                .url(socialMediaLink.getUrl())
                .socialMediaName(socialMediaLink.getSocialMediaName())
                .build();
    }
}
