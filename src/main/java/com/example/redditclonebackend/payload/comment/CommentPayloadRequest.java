package com.example.redditclonebackend.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentPayloadRequest {
    @NotBlank(message = "Content is required")
    private String content;
    @NotBlank(message = "Author username is required")
    private String authorUsername;
    private MultipartFile file;
}
