package com.studying.first_spring_app.dto.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studying.first_spring_app.model.TaskPriority;

public interface BaseTask {
    String title();
    TaskPriority priority();
    boolean completed();

    @JsonProperty("image_id")
    String imageId();
}
