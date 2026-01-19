package com.studying.first_spring_app.service;

import com.studying.first_spring_app.exception.TaskNotFoundException;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    private static final UUID FIXED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void isTaskOwner_shouldReturnTrue_whenTaskExistsAndUserIsOwner() {
        var user = User.builder().id(FIXED_ID).username("Test User").build();
        var taskUUID = UUID.fromString("10600504-3510-0000-0000-050700809001");
        var task = Task.builder().id(taskUUID).title("Test").user(user).build();

        Mockito.when(taskRepository.findById(taskUUID)).thenReturn(Optional.of(task));

        assertThat(securityService.isTaskOwner(task.getId(), user.getUsername())).isTrue();
    }

    @Test
    void isTaskOwner_shouldThrowException_whenTaskExistsButUserIsNotOwner() {
        var user = User.builder().id(FIXED_ID).username("Test User").build();
        var task = Task.builder().id(UUID.randomUUID()).title("Test").user(user).build();

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(TaskNotFoundException.class,
                () -> securityService.isTaskOwner(task.getId(), "another user"));
    }
}
