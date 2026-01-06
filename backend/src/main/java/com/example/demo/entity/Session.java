package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "sessions")
public class Session {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String userId;

    @Indexed(unique = true)
    private String refreshToken;

    @Indexed(expireAfter = "0s")
    private LocalDateTime expiresAt;

}
