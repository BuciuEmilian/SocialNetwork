package com.example.socialnetwork.repository.memory;

import com.example.socialnetwork.domain.User;

public class LoginRepository {
    private Long currentUserId;
    private String currentUsername;

    public Long getCurrentUserId() {
        return this.currentUserId;
    }

    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void login(User user) {
        this.currentUserId = user.getId();
        this.currentUsername = user.getUsername();
    }

    public void logout() {
        this.currentUserId = null;
        this.currentUsername = null;
    }
}
