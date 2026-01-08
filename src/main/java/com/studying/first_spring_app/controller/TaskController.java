package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.CreateTaskDto;
import com.studying.first_spring_app.dto.FileResponse;
import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.repository.TaskSpecification;
import com.studying.first_spring_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Object index(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String priority,

            @PageableDefault(size = 100, sort = "updatedAt")
            Pageable pageable) {
        Specification<Task> spec = TaskSpecification.all();

        if (title != null) {
            spec = spec.and(TaskSpecification.hasTitle(title));
        }
        if (completed != null) {
            spec = spec.and(TaskSpecification.hasCompletedStatus(completed));
        }
        if (priority != null) {
            spec = spec.and(TaskSpecification.hasPriority(priority));
        }

        boolean sortByPriority = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("priority"));

        if(sortByPriority) {
            boolean isDesc = pageable.getSort().getOrderFor("priority").isDescending();
            spec = spec.and(TaskSpecification.orderByPriority(isDesc));
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        return taskService.search(spec, pageable);
    }

    @GetMapping("{id}")
    public TaskDto getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public TaskDto addTask(@Valid @RequestBody CreateTaskDto dto) {
        return taskService.create(dto);
    }

    @PostMapping("{id}/image")
    public Object uploadImage(@PathVariable UUID id,
                              @RequestParam("file") MultipartFile file) {
        final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/webp",
                "image/svg+xml"
        );

        if (!SUPPORTED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type");
        }

        return taskService.uploadImage(id, file);
    }

    @GetMapping("{id}/image")
    public Object getImage(@PathVariable UUID id) {
        FileResponse imageResponse = taskService.getImage(id);
        return ResponseEntity.ok()
                .contentType(imageResponse.contentType())
                .body(new InputStreamResource(imageResponse.stream()));
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
