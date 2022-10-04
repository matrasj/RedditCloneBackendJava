package com.example.redditclonebackend.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserPayload {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Date createdAt;
    private Date lastUpdated;
    private boolean enabled;


}
