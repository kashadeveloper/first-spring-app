package com.studying.first_spring_app.dto;

import jakarta.validation.constraints.Size;

public record PatchTaskDto(
        @Size(max = 100, message = "Title is too large")
        String title,

        @Size(max = 400, message = "Description is too large")
        String description,

        Boolean completed
) {
}
