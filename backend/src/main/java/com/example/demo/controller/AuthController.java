package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UsernamePasswordRequest;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody UsernamePasswordRequest auth,
            HttpServletResponse response) {
        var res = authService.login(auth.getUsername(), auth.getPassword());

        addRefreshTokenCookie(response, res.getRefreshToken(), res.getRefreshTokenMaxAge());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UsernamePasswordRequest auth) {
        var res = authService.register(auth.getUsername(), auth.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response,
            @CookieValue("refresh_token") String refreshToken) {
        authService.logout(refreshToken);
        removeRefreshTokenCookie(response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest request,
            @CookieValue("refresh_token") String refreshToken) {
        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(RefreshResponse.builder()
                .message("Successfully refreshed")
                .accessToken(accessToken)
                .build());
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String value, int maxAge) {
        Cookie cookie = new Cookie("refresh_token", value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
