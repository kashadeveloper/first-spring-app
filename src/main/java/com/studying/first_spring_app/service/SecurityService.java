package com.studying.first_spring_app.service;

import com.studying.first_spring_app.exception.TaskNotFoundException;
import com.studying.first_spring_app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {
    private final TaskRepository taskRepository;

    public boolean isTaskOwner(UUID taskId, String username) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUser().getUsername().equals(username)) {
            throw new TaskNotFoundException();
        }
        return true;
    }
}
