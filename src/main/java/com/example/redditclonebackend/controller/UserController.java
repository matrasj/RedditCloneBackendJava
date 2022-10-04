package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.user.UserPayload;
import com.example.redditclonebackend.payload.user.UserPayloadResponse;
import com.example.redditclonebackend.payload.user.UserSearchPayload;
import com.example.redditclonebackend.service.ProfileImageService;
import com.example.redditclonebackend.service.UserAccountService;
import com.example.redditclonebackend.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserAccountService userAccountService;
    private final ProfileImageService imageUploadService;

    @GetMapping
    public ResponseEntity<Page<UserPayload>> findUsers(@RequestParam int pageNumber,
                                                       @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(userDetailsService.getUsersPage(pageNumber, pageSize));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserPayloadResponse> getSingleUser(@PathVariable String username) {
        return ResponseEntity.status(OK)
                .body(userDetailsService.findUserByUsername(username));
    }

    @GetMapping("/usernames")
    public ResponseEntity<List<UserSearchPayload>> getSearchedUsers(@RequestParam String username) {
        return ResponseEntity.status(OK)
                .body(userDetailsService.findSearchedUsers(username));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestBody MultipartFile profileImage) throws IOException {
        return ResponseEntity.status(CREATED)
                .body(imageUploadService.uploadProfileImage(profileImage));
    }

    @PostMapping("/biography")
    public ResponseEntity<String> setBiography(@RequestBody String biography) {
        return ResponseEntity.status(ACCEPTED)
                .body(userAccountService.setUserBiography(biography));
    }

    @GetMapping("/biography/delete")
    public ResponseEntity<String> deleteBiography() {
        return ResponseEntity.status(ACCEPTED)
                .body(userAccountService.deleteBiography());
    }

}
