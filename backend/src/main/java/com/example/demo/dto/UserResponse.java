package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String username;
}
