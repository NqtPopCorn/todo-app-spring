package com.example.demo.service;

import com.example.demo.dto.UserResponse;

public interface UserService {
    UserResponse getUser(String userId);

    void changePassword(String userId, String oldPassword, String newPassword);
    // void updatePassword(String userId, String newPassword);
}
