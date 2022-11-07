package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.UserPrincipal;
import com.example.redditclonebackend.jwt.JwtTokenProvider;
import com.example.redditclonebackend.payload.auth.login.LoginRequest;
import com.example.redditclonebackend.service.auth.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.AutoConfigureDataCassandra;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureTestDatabase
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    AuthService authService;

    @MockBean
    UserDetailsService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        reset(userService);
    }

   @Test
    void login() throws Exception {
       var loginRequest = new LoginRequest("admin", "admin");
       var user = buildUser();

        assertAll("Failed with building user",
                () -> assertEquals("admin", user.getUsername(), "Failed with username"),
                () -> assertEquals("admin", user.getPassword(), "Failed with password"),
                () -> assertEquals("admin.admin@gmail.com", user.getEmail(), "Failed with emial")
        );


        UserDetails userDetails = new UserPrincipal(user);
        String loginRequestAsString = objectMapper.writeValueAsString(loginRequest);
        given(authenticationManager.authenticate(any()))
                .willReturn(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()));

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(loginRequestAsString)
                        .with(csrf())
        ).andExpect(status().isOk());

   }

   private User buildUser() {
        return User.builder()
                .id(1L)
                .username("admin")
                .password("admin")
                .email("admin.admin@gmail.com")
                .enabled(true)
                .build();
   }
}