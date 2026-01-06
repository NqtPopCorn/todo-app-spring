package com.example.demo.repository;

import com.example.demo.entity.Session;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, String> {
    Optional<Session> findByRefreshToken(String refreshToken);

    Optional<Session> findByUserId(String userId);
}
