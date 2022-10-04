package com.example.redditclonebackend.entity;

public enum VoteType {
    VOTE_UP(1), VOTE_DOWN(-1);

    private int direction;

    VoteType(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
