package com.example.socialnetwork.domain;

import java.time.LocalDateTime;

import static com.example.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class Friendship extends Entity<Pair<Long>> {
    private LocalDateTime friendsFrom;
    private String status;

    public Friendship(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
        status = "pending";
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "userId='" + this.getId().getFirst() + '\'' +
                ", friendId='" + this.getId().getSecond() + '\'' +
                ", friendsFrom='" + this.getFriendsFrom().format(DATE_TIME_FORMATTER) + '\'' +
                "}";
    }
}
