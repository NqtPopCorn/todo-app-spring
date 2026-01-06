package com.example.demo.service.impl;

import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(404, "User not found")
        );
        return UserResponse.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ApplicationException(404, "User not found");
        }
        if(!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new ApplicationException(403, "Wrong password");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
