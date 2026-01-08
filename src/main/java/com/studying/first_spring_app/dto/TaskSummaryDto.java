package com.studying.first_spring_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.studying.first_spring_app.dto.utils.BaseTask;
import com.studying.first_spring_app.dto.utils.HasId;
import com.studying.first_spring_app.dto.utils.HasUpdateDate;
import com.studying.first_spring_app.model.TaskPriority;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TaskSummaryDto(UUID id, String title, boolean completed, String imageId,
                             TaskPriority priority, LocalDateTime updatedAt) implements HasId, HasUpdateDate, BaseTask {
}
