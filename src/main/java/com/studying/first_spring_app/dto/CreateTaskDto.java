package com.studying.first_spring_app.dto;

import com.studying.first_spring_app.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskDto(@NotBlank(message = "Title is required")
                            @Size(max = 100, message = "Title is too large")
                            String title,

                            @Size(max = 400, message = "Description is too large")
                            String description,
                            Boolean completed,
                            TaskPriority priority) {
}
