package com.studying.first_spring_app.dto.utils;

import com.studying.first_spring_app.model.TaskPriority;

public interface BaseTask {
    String title();
    TaskPriority priority();
    boolean completed();

    String imageId();
}
