package com.example.redditclonebackend.payload.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSearchPayload {
    private String username;
    private String profileImageLink;
}
