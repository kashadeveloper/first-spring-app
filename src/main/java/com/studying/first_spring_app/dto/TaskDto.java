package com.studying.first_spring_app.dto;

import com.studying.first_spring_app.dto.utils.BaseTask;
import com.studying.first_spring_app.dto.utils.HasCreationDate;
import com.studying.first_spring_app.dto.utils.HasId;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(UUID id, String title, String description, boolean completed,
                      LocalDateTime createdAt, String imageId) implements BaseTask, HasId, HasCreationDate {
}
