package com.studying.first_spring_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.studying.first_spring_app.dto.utils.BaseTask;
import com.studying.first_spring_app.dto.utils.HasId;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TaskSummaryDto(UUID id, String title, boolean completed, String imageId) implements HasId, BaseTask {
}
