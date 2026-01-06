package com.example.demo.dto;

import com.example.demo.entity.Todo;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoFilterDto {
    private LocalDateTime from;
    private LocalDateTime to;

    @Pattern(regexp = "TODO|WORKING|COMPLETED", message = "Invalid status value")
    private String status;
}
