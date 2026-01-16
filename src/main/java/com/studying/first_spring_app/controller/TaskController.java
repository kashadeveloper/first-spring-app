package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.CreateTaskDto;
import com.studying.first_spring_app.dto.FileResponse;
import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.TaskSpecification;
import com.studying.first_spring_app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@Tag(name = "Tasks")
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Get tasks list")
    @GetMapping
    public Object index(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean completed,

            @RequestParam(required = false)
            @Schema(allowableValues = {"LOW", "MEDIUM", "HIGH"})
            String priority,

            @PageableDefault(size = 100, sort = "updatedAt")
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        Specification<Task> spec = TaskSpecification.byUser(user.getId());

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

        if (sortByPriority) {
            boolean isDesc = pageable.getSort().getOrderFor("priority").isDescending();
            spec = spec.and(TaskSpecification.orderByPriority(isDesc));
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        return taskService.search(spec, pageable);
    }

    @Operation(summary = "Get task info by id")
    @GetMapping("{id}")
    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
    public TaskDto getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @Operation(summary = "Create new task")
    @PostMapping
    public TaskDto addTask(@Valid @RequestBody CreateTaskDto dto,
                           @AuthenticationPrincipal User user) {

        return taskService.create(dto, user);
    }

    @Operation(summary = "Set task image")
    @PostMapping("{id}/image")
    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
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

    @Operation(summary = "Get task image")
    @GetMapping("{id}/image")
    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
    public Object getImage(@PathVariable UUID id) {
        FileResponse imageResponse = taskService.getImage(id);
        return ResponseEntity.ok()
                .contentType(imageResponse.contentType())
                .body(new InputStreamResource(imageResponse.stream()));
    }

    @Operation(summary = "Update task info by id")
    @PatchMapping("{id}")
    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
    public TaskDto updateTask(@Valid @RequestBody PatchTaskDto dto, @PathVariable UUID id) {
        return taskService.updateTask(id, dto);
    }

    @Operation(summary = "Delete task by id")
    @DeleteMapping("{id}")
    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
    }

    @Operation(summary = "Delete multiple tasks by id")
    @DeleteMapping
//    @PreAuthorize("@securityService.isTaskOwner(#id, authentication.name)")
    public Object deleteAllTasksByIds(@RequestBody List<UUID> ids, @AuthenticationPrincipal User user) {
        taskService.deleteAllTasksByIds(ids, user.getId());
        return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
    }
}
