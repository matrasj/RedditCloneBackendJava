package com.example.redditclonebackend.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CommentPayloadResponse {
    private Long id;
    private String content;
    private Date createdAt;
    private String authorUsername;
    private Long postId;
    private String profileImageLink;
    private String commentImageLink;
}
