package com.example.redditclonebackend.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentPayloadRequest {
    private String content;
    private String authorUsername;
    private MultipartFile file;
}
