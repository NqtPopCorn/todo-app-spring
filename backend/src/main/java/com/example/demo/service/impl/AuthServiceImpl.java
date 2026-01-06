package com.example.demo.service.impl;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Session;
import com.example.demo.entity.User;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${auth.jwt.secret}")
    private String JWT_SECRET;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    private int REFRESH_TOKEN_TTL = 14 * 24 * 60 * 60; // 14 days
    private long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 1 day

    @Override
    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(404, "Username not found"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ApplicationException(401, "Incorrect password");
        }

        // generate access token, refresh token
        String accessToken = generateJwtToken(user.getId(), JWT_EXPIRATION);
        String refreshToken = generateSecureToken();

        // save refresh token to db
        Session existingSession = sessionRepository.findByUserId(user.getId()).orElse(null);
        LocalDateTime refreshTokenExpiredAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TTL);
        if (existingSession == null) {
            sessionRepository.save(Session.builder()
                    .userId(user.getId())
                    .expiresAt(refreshTokenExpiredAt)
                    .refreshToken(refreshToken)
                    .build());
        } else {
            existingSession.setRefreshToken(refreshToken);
            existingSession.setExpiresAt(refreshTokenExpiredAt);
            sessionRepository.save(existingSession);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenMaxAge(REFRESH_TOKEN_TTL)
                .message("Success")
                .build();
    }

    @Override
    public void logout(String token) {
        // remove session with this token
        sessionRepository.findByRefreshToken(token)
                .ifPresent(sessionRepository::delete);
    }

    @Override
    public UserResponse register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new ApplicationException(409, "Username already exists");
        }
        // create new user
        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now())
                .build();
        return toUserResponse(userRepository.save(user));
    }

    @Override
    public String refreshToken(String refreshToken) {
        // check the existing session then generate new token if valid
        Session session = sessionRepository.findByRefreshToken(refreshToken).orElse(null);
        if (session != null) {
            if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                return generateJwtToken(session.getUserId(), JWT_EXPIRATION);
            }
        }
        throw new ApplicationException(401, "Refresh token not found or expired");
    }

    @Override
    public UserResponse verifyToken(String token) {
        // parse token -> get userId -> userResponse
        String userId = verifyAccessToken(token).getSubject();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ApplicationException(404, "User not found");
        }

        // map user to user response
        return UserResponse.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .build();
    }

    private String generateJwtToken(String subject, long expiryTime) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .expirationTime(new Date(System.currentTimeMillis() + expiryTime))
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(new MACSigner(JWT_SECRET));
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new ApplicationException(500, "Error signing JWT");

        }
    }

    private String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[64];
        secureRandom.nextBytes(tokenBytes);
        // Using Base64 is generally more efficient for storage and transmission than
        // hex
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private JWTClaimsSet verifyAccessToken(String jwtToken) {
        try {
            JWSVerifier verifier = new MACVerifier(JWT_SECRET);
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
            boolean verified = signedJWT.verify(verifier);
            if (!verified) {
                throw new ApplicationException(401, "Invalid token");
            }
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new ApplicationException(403, "Expired token");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            if(e instanceof ApplicationException) {
                throw (ApplicationException) e;
            }
            throw new ApplicationException(500, "Error signing JWT");
        }
    }
}
