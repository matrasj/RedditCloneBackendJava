package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.SocialMediaLink;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadRequest;
import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadResponse;
import com.example.redditclonebackend.repository.SocialMediaLinkRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialMediaLinkService {
    private static final String SUCCESSFULLY_CREATED_SOCIAL_MEDIA_LINK = "Successfully created link!";
    private static final String SUCCESSFULLY_EDITED_SOCIAL_MEDIA_LINK = "Successfully edited link!";
    private static final String SUCCESSFULLY_DELETED_SOCIAL_MEDIAL_LINK = "Successfully deleted link!";
    private final SocialMediaLinkRepository socialMediaLinkRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public String createLinkAndAssignToCurrentAccount(SocialMediaLinkPayloadRequest
                                                              socialMediaLinkPayload) {
        User currentUser = authService.getCurrentUser();
        SocialMediaLink socialMediaLink = SocialMediaLink.builder()
                .socialMediaName(socialMediaLinkPayload.getSocialMediaName())
                .url(socialMediaLinkPayload.getUrl())
                .user(currentUser)
                .build();

        currentUser.getLinks().add(socialMediaLink);

        socialMediaLinkRepository.save(socialMediaLink);
        userRepository.save(currentUser);

        return SUCCESSFULLY_CREATED_SOCIAL_MEDIA_LINK;

    }

    public String editSocialMediaLink(SocialMediaLinkPayloadResponse socialMediaLink) {
        SocialMediaLink existingLink = socialMediaLinkRepository.findById(socialMediaLink.getId())
                .orElseGet(() -> SocialMediaLink.builder()
                        .user(authService.getCurrentUser())
                        .socialMediaName(socialMediaLink.getSocialMediaName())
                        .url(socialMediaLink.getUrl())
                        .build());

        existingLink.setSocialMediaName(socialMediaLink.getSocialMediaName());
        existingLink.setUrl(socialMediaLink.getUrl());

        socialMediaLinkRepository.save(existingLink);

        return SUCCESSFULLY_EDITED_SOCIAL_MEDIA_LINK;
    }

    public String deleteSocialMediaLinkById(Long socialMediaLinkId) {
        socialMediaLinkRepository.deleteById(socialMediaLinkId);

        return SUCCESSFULLY_DELETED_SOCIAL_MEDIAL_LINK;
    }
}
