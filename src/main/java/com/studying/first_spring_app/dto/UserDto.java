package com.studying.first_spring_app.dto;

import com.studying.first_spring_app.dto.utils.HasCreationDate;
import com.studying.first_spring_app.dto.utils.HasId;
import com.studying.first_spring_app.dto.utils.HasUpdateDate;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(UUID id, String username, LocalDateTime updatedAt,
                      LocalDateTime createdAt) implements HasId, HasCreationDate, HasUpdateDate {
}
