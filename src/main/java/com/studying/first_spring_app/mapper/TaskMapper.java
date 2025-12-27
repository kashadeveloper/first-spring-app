package com.studying.first_spring_app.mapper;

import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.model.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

// Can be made by MapStruct
@Component
public class TaskMapper {
    public Task toEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setId(taskDto.getId());
        if (taskDto.getCompleted() != null) {
            task.setCompleted(taskDto.getCompleted());
        }
        return task;
    }
    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setCompleted(task.isCompleted());
        taskDto.setId(task.getId());
        return taskDto;
    }

    public ArrayList<TaskDto> toDto(ArrayList<Task> tasks) {
        ArrayList<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : tasks) {
            taskDtos.add(toDto(task));
        }
        return taskDtos;
    }
}
