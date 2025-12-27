package com.studying.first_spring_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskDto {
    private int id;
    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 400, message = "Max size of description - 450 chars")
    private String description;
    private Boolean completed;
}
