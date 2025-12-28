package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.mapper.TaskMapper;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ArrayList<TaskDto> index() {
        return taskService.getTaskList();
    }

    @PostMapping
    public ArrayList<TaskDto> addTask(@Valid @RequestBody TaskDto dto) {
        taskService.newTask(dto);
        return taskService.getTaskList();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
    }
}
