package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostAndAuthor(@Param("post") Post post, @Param("author") User author);
    Optional<Vote> findByPostIdAndAuthorUsername(@Param("post") Long postId, @Param("author") String username);
}
