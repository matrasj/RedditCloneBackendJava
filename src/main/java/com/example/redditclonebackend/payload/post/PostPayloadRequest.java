package com.example.redditclonebackend.payload.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class PostPayloadRequest {
    private String title;
    private String content;
    private MultipartFile file;

}
