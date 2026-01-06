package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserResponse;

public interface AuthService {
    AuthResponse login(String username, String password);

    void logout(String token);

    UserResponse register(String username, String password);

    /**
     * Refresh access token
     * @return newAccessToken
     */
    String refreshToken(String refreshToken);

    UserResponse verifyToken(String token);
}
