package com.studying.first_spring_app.model.listeners;

import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.TaskPriority;
import com.studying.first_spring_app.service.FileStorageService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskListener {
    private final FileStorageService fileStorageService;

    @PreRemove
    public void onPreRemove(Task task) {
        var image = task.getImageId();
        if(image == null || image.isBlank()) {
            return;
        }

        fileStorageService.removeObject(image);
        log.info("Removed image {} on task deletion {}", image, task.getId());
    }

    @PrePersist
    public void prePersist(Task task) {
        if(task.getPriority() == null) {
            task.setPriority(TaskPriority.LOW);
        }
    }
}
