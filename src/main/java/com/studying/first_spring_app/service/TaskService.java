package com.studying.first_spring_app.service;

import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.mapper.TaskMapper;
import com.studying.first_spring_app.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskService {
    private TaskMapper taskMapper;
    private TaskRepository taskRepository;

    public TaskService(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    public ArrayList<TaskDto> newTask(TaskDto dto) {
        var task = taskMapper.toEntity(dto);
        taskRepository.addTask(task);
        return taskMapper.toDto(taskRepository.getTaskList());
    }

    public void deleteTask(int id) {
        taskRepository.removeTask(id);
    }

    public ArrayList<TaskDto> getTaskList() {
        return taskMapper.toDto(taskRepository.getTaskList());
    }
}
