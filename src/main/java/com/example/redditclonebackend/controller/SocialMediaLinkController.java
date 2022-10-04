package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadRequest;
import com.example.redditclonebackend.payload.socialmedialink.SocialMediaLinkPayloadResponse;
import com.example.redditclonebackend.service.SocialMediaLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/social-media-links")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SocialMediaLinkController {
    private final SocialMediaLinkService socialMediaLinkService;
    @PostMapping
    public ResponseEntity<String> createSocialMediaLinkAndAssignToCurrentAccount(@RequestBody SocialMediaLinkPayloadRequest
                                                                                             socialMediaLinkPayload) {
        return ResponseEntity.status(CREATED)
                .body(socialMediaLinkService.createLinkAndAssignToCurrentAccount(socialMediaLinkPayload));
    }

    @PutMapping
    public ResponseEntity<String> editSocialMediaLink(@RequestBody SocialMediaLinkPayloadResponse socialMediaLink) {
        return ResponseEntity.status(ACCEPTED)
                .body(socialMediaLinkService.editSocialMediaLink(socialMediaLink));
    }

    @GetMapping("/delete/{socialMediaLinkId}")
    public ResponseEntity<String> deleteSocialMediaLink(@PathVariable Long socialMediaLinkId) {
        return ResponseEntity.status(ACCEPTED)
                .body(socialMediaLinkService.deleteSocialMediaLinkById(socialMediaLinkId));
    }




}
