package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.Comment;
import com.example.redditclonebackend.payload.comment.CommentPayloadResponse;

public class CommentPayloadResponseMapper {
    public static CommentPayloadResponse mapToCommentPayloadResponse(Comment comment) {
        return CommentPayloadResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorUsername(comment.getAuthor().getUsername())
                .postId(comment.getPost().getId())
                .profileImageLink(comment.getAuthor().getProfileImageLink())
                .commentImageLink(comment.getImageLink())
                .build();
    }
}
