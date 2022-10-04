package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.entity.SocialMediaLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaLinkRepository extends JpaRepository<SocialMediaLink, Long> {
}
