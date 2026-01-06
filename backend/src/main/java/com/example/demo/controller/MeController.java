package com.example.demo.controller;

import com.example.demo.dto.UpdatePasswordRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserProfile(
            @AuthenticationPrincipal String userId) {
        var res = userService.getUser(userId);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUserPassword(
            @RequestBody UpdatePasswordRequest updatePasswordRequest,
            @AuthenticationPrincipal String userId) {
        userService.changePassword(userId, updatePasswordRequest.getOldPassword(),
                updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Success");
    }
}
