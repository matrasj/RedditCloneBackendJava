package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {
    private static final String SUCCESSFULLY_SET_PROFILE_IMAGE = "Success!";
    private final String FULL_PATH = "C:\\Users\\jkobm\\WebstormProjects\\CourseManagmentSystemFrontEnd\\src\\";
    private final String PROJECT_PATH = "assets\\images\\profile-images\\";
    private final AuthService authService;
    private final UserRepository userRepository;
    private final StorageService storageService;


    @Transactional
    public String uploadProfileImage(MultipartFile file) throws IOException {
        User user = authService.getCurrentUser();

        if (user.getProfileImageLink() != null) {
            String[] folderAndFileName = user.getProfileImageLink().replace("https://mybucket-reddit.s3.eu-central-1.amazonaws.com/", "").split("/");
            storageService.deleteImage(folderAndFileName[0], folderAndFileName[1]);
        }

        if (file != null) {
            String photoLink = storageService.saveImage(file, "profile-images");
            user.setProfileImageLink(photoLink);
        }

        userRepository.save(user);

        return SUCCESSFULLY_SET_PROFILE_IMAGE;
    }
}
