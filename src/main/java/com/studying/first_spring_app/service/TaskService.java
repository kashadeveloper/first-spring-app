package com.studying.first_spring_app.service;

import com.studying.first_spring_app.dto.*;
import com.studying.first_spring_app.exception.TaskAlreadyExistsException;
import com.studying.first_spring_app.exception.TaskNotFoundException;
import com.studying.first_spring_app.mapper.TaskMapper;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.TaskRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static com.studying.first_spring_app.config.RedisConfig.CACHE_TASK;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final FileStorageService fileStorageService;
    private final CacheManager cacheManager;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, FileStorageService fileStorageService, CacheManager cacheManager) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.fileStorageService = fileStorageService;
        this.cacheManager = cacheManager;
    }

    @Transactional
    public TaskDto create(CreateTaskDto dto, User user) {
        var task = taskMapper.toEntity(dto);
        if (taskRepository.existsByTitle(dto.title())) {
            throw new TaskAlreadyExistsException();
        }
        task.setUser(user);
        return taskMapper.toDetailedDto(taskRepository.saveAndFlush(task));
    }

    @Transactional
    @CacheEvict(value = CACHE_TASK, key = "#id")
    public TaskDto updateTask(UUID id, PatchTaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        if (taskRepository.existsByTitleAndIdNot(dto.title(), task.getId())) {
            throw new TaskAlreadyExistsException("Task with title '" + dto.title() + "' already exists");
        }

        taskMapper.patchEntity(dto, task);

        return taskMapper.toDetailedDto(taskRepository.saveAndFlush(task));
    }

    @Transactional
    @CacheEvict(value = CACHE_TASK, key = "#id")
    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    @Cacheable(value = CACHE_TASK, key = "#id")
    public TaskDto getTask(UUID id, UUID userId) {
        return taskMapper.toDetailedDto(taskRepository.findByIdAndUserId(id, userId).orElseThrow(TaskNotFoundException::new));
    }

    @Transactional
    @CacheEvict(value = CACHE_TASK, key = "#ids")
    public void deleteAllTasksByIds(List<UUID> ids, UUID userId) {
        if (ids.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Ids is empty");
        }
        taskRepository.deleteAllByIds(ids, userId);

        var cache = cacheManager.getCache(CACHE_TASK);

        if (cache != null) {
            for (UUID id : ids) {
                cache.evict(id);
            }
        }
    }

    @Transactional
    public TaskDto uploadImage(UUID id, MultipartFile file) {
        var task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        var imageId = UUID.randomUUID().toString();
        if (!task.getImageId().isBlank()) {
            fileStorageService.removeObject(task.getImageId());
        }
        var fileName = fileStorageService.upload(file, imageId);
        task.setImageId(fileName);

        return taskMapper.toDetailedDto(taskRepository.saveAndFlush(task));
    }

    public FileResponse getImage(UUID id, UUID userId) {
        var task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(TaskNotFoundException::new);
        if (task.getImageId().isBlank()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Task not have image");
        }
        var stream = fileStorageService.getObject(task.getImageId());

        MediaType contentType = MediaTypeFactory.getMediaType(task.getImageId())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return new FileResponse(stream, task.getImageId(), contentType);
    }

    public List<TaskSummaryDto> search(Specification<Task> spec, Pageable pageable) {
        var tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::toSummaryDto).getContent();
    }
}
