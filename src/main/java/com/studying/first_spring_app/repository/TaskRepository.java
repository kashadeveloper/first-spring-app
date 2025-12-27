package com.studying.first_spring_app.repository;

import com.studying.first_spring_app.exception.NotFoundException;
import com.studying.first_spring_app.model.Task;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Data
public class TaskRepository {
    @Getter
    private final ArrayList<Task> taskList = new ArrayList<>();

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int nextId;

    public void addTask(Task task) {
        nextId++;
        task.setId(nextId);
        taskList.add(task);
    }

    public void removeTask(Task task) {
        nextId--;
        taskList.remove(task);
    }

    public void removeTask(int id) {
        nextId--;
        Task task = getById(id);
        taskList.remove(task);
    }

    public Task getById(int id) {
        return taskList.stream().filter(x -> x.getId() == id).findFirst().orElseThrow(() -> new NotFoundException("Task not found"));
    }
}
