package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {
    @Size(min = 1, max = 100, message = "Title length should be from 1 to 100")
    private String title;

    private String description;

    @Pattern(regexp = "^TODO|WORKING|COMPLETED$", message = "Status must be either TODO or COMPLETED")
    private String status;
}
