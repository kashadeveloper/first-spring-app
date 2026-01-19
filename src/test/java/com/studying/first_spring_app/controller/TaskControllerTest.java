package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.BaseIT;
import com.studying.first_spring_app.dto.CreateTaskDto;
import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.exception.TaskNotFoundException;
import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.TaskPriority;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.TaskRepository;
import com.studying.first_spring_app.repository.UserRepository;
import com.studying.first_spring_app.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TaskControllerTest extends BaseIT {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private WebTestClient webTestClient;

    String token;
    User user;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        var user = new User();
        user.setUsername("Test User");
        user.setPassword("Test Password");
        this.user = userRepository.save(user);

        this.token = jwtService.generateToken(user);
    }

    @Test
    void shouldGetAllTasks() {
        var task_1 = new Task();
        task_1.setTitle("Task 1");
        task_1.setDescription("Task 1 description");
        task_1.setUser(user);

        var task_2 = new Task();
        task_2.setTitle("Task 2");
        task_2.setDescription("Task 2 description");
        task_2.setUser(user);

        taskRepository.saveAll(List.of(task_1, task_2));

        authorize(webTestClient.get().uri("/tasks"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskDto.class)
                .consumeWith(taskDtos -> {
                    var body = taskDtos.getResponseBody();
                    assertThat(body).isNotEmpty();
                    assertThat(body.size()).isEqualTo(2);
                    assertThat(body.get(0).title()).isEqualTo("Task 1");
                    assertThat(body.get(1).title()).isEqualTo("Task 2");
                });

    }

    @Test
    void shouldGetTaskById() {
        var task = Task.builder().title("Test task").user(user).build();
        var newTask = taskRepository.save(task);

        authorize(webTestClient.get().uri("/tasks/" + newTask.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .consumeWith(taskDto -> {
                    var body = taskDto.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.id()).isEqualTo(newTask.getId());
                });
    }

    @Test
    void shouldReturn404_WhenTaskDoesNotExist() {
        var randomId = UUID.randomUUID().toString();
        authorize(webTestClient.get().uri("/tasks/" + randomId))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TaskNotFoundException.class);
    }

    @Test
    void shouldCreateTask() {
        var dto = new CreateTaskDto("Create task sample", "", null, TaskPriority.HIGH);
        authorize(webTestClient.post()
                .uri("/tasks")
                .bodyValue(dto)).exchange().expectStatus().isOk().expectBody(TaskDto.class);
    }

    @Test
    void shouldReturn409_WhenTaskAlreadyExists() {
        var task = Task.builder().title("Create task sample").priority(TaskPriority.HIGH).user(user).build();
        taskRepository.save(task);

        authorize(webTestClient.post().uri("/tasks").bodyValue(new CreateTaskDto("Create task sample", null, null, TaskPriority.HIGH)))
                .exchange().expectStatus().isEqualTo(409);
    }

    @Test
    void shouldDeleteTask() {
        var task = taskRepository.save(Task.builder().title("Delete task sample").user(user).build());
        authorize(webTestClient.delete().uri("/tasks/" + task.getId())).exchange().expectStatus().isOk();
    }

    @Test
    void shouldReturn404_WhenDeleteTaskDoesNotExist() {
        authorize(webTestClient.get().uri("/tasks/" + UUID.randomUUID())).exchange().expectStatus().isNotFound();
    }

    @Test
    void shouldPatchTask() {
        var task = taskRepository.save(Task.builder().title("Update task sample").user(user).build());
        var dto = new PatchTaskDto("New title", "Test", null, TaskPriority.HIGH);
        authorize(webTestClient.patch().uri("/tasks/" + task.getId()).bodyValue(dto)).exchange().expectStatus().isOk();

        var foundTask = taskRepository.findById(task.getId()).orElse(null);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo(dto.title());
        assertThat(foundTask.getDescription()).isEqualTo(dto.description());
        assertThat(foundTask.getPriority()).isEqualTo(dto.priority());
    }

    @Test
    void shouldReturn404_WhenPatchTaskThatDoesNotForThisUser() {
        var firstTask = Task.builder().title("Update task sample for first user").user(user).build();
        taskRepository.save(firstTask);

        var newUser = User.builder().username("New user").password("<>").build();
        userRepository.save(newUser);

        var tokenForNewUser = jwtService.generateToken(newUser);

        var dto = new CreateTaskDto("New title", "Test", null, TaskPriority.HIGH);
        webTestClient.patch().uri("/tasks/" + firstTask.getId()).bodyValue(dto)
                .header("Authorization", "Bearer " + tokenForNewUser)
                .exchange().expectStatus().isNotFound();

        var foundTask = taskRepository.findById(firstTask.getId()).orElse(null);
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo("Update task sample for first user");
        assertThat(foundTask.getDescription()).isEmpty();
        assertThat(foundTask.getPriority()).isEqualTo(TaskPriority.LOW);
    }

    @Test
    void shouldReturn404_WhenDeleteTaskThatDoesNotForThisUser() {
        var firstTask = Task.builder().title("Delete task sample for first user").user(user).build();
        taskRepository.save(firstTask);

        var newUser = User.builder().username("New user").password("<>").build();
        userRepository.save(newUser);

        var tokenForNewUser = jwtService.generateToken(newUser);

        webTestClient.delete().uri("/tasks/" + firstTask.getId())
                .header("Authorization", "Bearer " + tokenForNewUser)
                .exchange().expectStatus().isNotFound();

        var foundTask = taskRepository.findById(firstTask.getId()).orElse(null);
        assertThat(foundTask).isNotNull();
    }

    @Test
    void shouldReturn404_WhenFindTaskThatDoesNotForThisUser() {
        var firstTask = Task.builder().title("Task sample for first user").user(user).build();
        taskRepository.save(firstTask);

        var newUser = User.builder().username("New user").password("<>").build();
        userRepository.save(newUser);

        var tokenForNewUser = jwtService.generateToken(newUser);

        webTestClient.get().uri("/tasks/" + firstTask.getId())
                .header("Authorization", "Bearer " + tokenForNewUser)
                .exchange().expectStatus().isNotFound();
    }

    private WebTestClient.RequestHeadersSpec<?> authorize(WebTestClient.RequestHeadersSpec<?> spec) {
        return spec.header("Authorization", "Bearer " + token);
    }
}
