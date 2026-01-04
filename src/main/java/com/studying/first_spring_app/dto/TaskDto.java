package com.studying.first_spring_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskDto(
        UUID id,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title is too large")
        String title,

        @Size(max = 400, message = "Description is too large")
        String description,
        Boolean completed,

        String image_id
) {
}
