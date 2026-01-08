package com.studying.first_spring_app.dto.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public interface HasUpdateDate {
    @JsonProperty("updated_at")
    LocalDateTime updatedAt();
}
