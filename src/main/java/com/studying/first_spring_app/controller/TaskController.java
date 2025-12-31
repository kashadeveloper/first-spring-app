package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> index() {
        return taskService.getTaskList();
    }

    @GetMapping("{id}")
    public TaskDto getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public TaskDto addTask(@Valid @RequestBody TaskDto dto) {
        return taskService.newTask(dto);
    }

    @PatchMapping("{id}")
    public TaskDto updateTask(@Valid @RequestBody PatchTaskDto dto, @PathVariable UUID id) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
    }

    @DeleteMapping
    public Object deleteAllTasksByIds(@RequestBody List<UUID> ids) {
        taskService.deleteAllTasksByIds(ids);
        return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
    }
}
