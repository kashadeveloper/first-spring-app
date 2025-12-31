package com.studying.first_spring_app.service;

import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.exception.TaskAlreadyExistsException;
import com.studying.first_spring_app.exception.TaskNotFoundException;
import com.studying.first_spring_app.mapper.TaskMapper;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.repository.TaskRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public TaskDto newTask(TaskDto dto) {
        var task = taskMapper.toEntity(dto);
        if (taskRepository.existsByTitle(dto.title())) {
            throw new TaskAlreadyExistsException();
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public TaskDto updateTask(UUID id, PatchTaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        if (taskRepository.existsByTitleAndIdNot(dto.title(), task.getId())) {
            throw new TaskAlreadyExistsException("Task with title '" + dto.title() + "' already exists");
        }

        taskMapper.patchEntity(dto, task);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    public TaskDto getTask(UUID id) {
        return taskMapper.toDto(taskRepository.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    @Transactional
    public void deleteAllTasksByIds(List<UUID> ids) {
        if (ids.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Ids is empty");
        }
        taskRepository.deleteAllByIds(ids);
    }

    public List<TaskDto> getTaskList() {
        return taskMapper.toDto(taskRepository.findAll());
    }
}
