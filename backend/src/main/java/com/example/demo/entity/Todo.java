package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "todos")
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String id;

    @Indexed
    private String userId;

    private String title;
    private String description;

    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public enum Status {
        TODO,
        WORKING,
        COMPLETED
    }

}
