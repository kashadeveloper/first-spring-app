package com.studying.first_spring_app.dto;

import com.studying.first_spring_app.dto.utils.BaseTask;
import com.studying.first_spring_app.dto.utils.HasCreationDate;
import com.studying.first_spring_app.dto.utils.HasId;
import com.studying.first_spring_app.model.TaskPriority;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(UUID id, String title, String description, boolean completed,
                      LocalDateTime createdAt, String imageId, TaskPriority priority) implements BaseTask, HasId, HasCreationDate {
}
