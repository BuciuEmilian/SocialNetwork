package com.example.socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendDTO {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String dateTime;

    public FriendDTO(Long id, String username, String firstName, String lastName, String dateTime) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
