package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.BaseIT;
import com.studying.first_spring_app.dto.AuthRequest;
import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.dto.TokensResponse;
import com.studying.first_spring_app.exception.UserAlreadyExistsException;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthControllerTest extends BaseIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        var dto = new CreateUserDto("Test User", "testpass1234");
        var cookie = webTestClient.post().uri("/auth/register")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokensResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.accessToken()).isNotNull();
                    assertThat(response.refreshToken()).isNotNull();
                })
                .returnResult()
                .getResponseCookies()
                .getFirst("rf");
        assertThat(cookie).isNotNull();
        assertThat(cookie.isHttpOnly()).isTrue();

        assertThat(userRepository.findByUsername("Test User")).isPresent();
    }

    @Test
    void shouldReturn409_WhenUserExists() {
        userRepository.save(User.builder().username("Test User").password("testpass1234").build());

        var dto = new CreateUserDto("Test User", "testpass1234");

        webTestClient.post().uri("/auth/register").bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.message").isEqualTo(new UserAlreadyExistsException().getMessage());
    }

    @Test
    void shouldReturn400_WhenRegisterUserNameIsNull() {
        var dto = new CreateUserDto(null, "testpass1234");
        webTestClient.post().uri("/auth/register").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(400);
    }

    @Test
    void shouldReturn400_WhenRegisterPasswordIsNull() {
        var dto = new CreateUserDto("Test User", null);
        webTestClient.post().uri("/auth/register").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(400);
    }

    @Test
    void shouldReturn400_WhenRegisterPasswordIsEmpty() {
        var dto = new CreateUserDto("Test User", "");
        webTestClient.post().uri("/auth/register").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(400);
    }

    @Test
    void shouldReturn400_WhenRegisterUserNameIsEmpty() {
        var dto = new CreateUserDto("", "testpass1234");
        webTestClient.post().uri("/auth/register").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(400);
    }

    @Test
    void shouldLogInUser() {
        userRepository.save(User.builder().username("Test User").password(passwordEncoder.encode("testpass1234")).build());
        var dto = new AuthRequest("Test User", "testpass1234");
        var cookie = webTestClient.post().uri("/auth/login").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(200).expectBody(TokensResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.accessToken()).isNotNull();
                    assertThat(response.refreshToken()).isNotNull();
                }).returnResult()
        .getResponseCookies()
        .getFirst("rf");

        assertThat(cookie).isNotNull();
        assertThat(cookie.isHttpOnly()).isTrue();
    }

    @Test
    void shouldReturn401_WhenPasswordIsNotCorrect() {
        userRepository.save(User.builder().username("Test User").password(passwordEncoder.encode("test123")).build());

        var dto = new AuthRequest("Test User", "testpass1234");
        webTestClient.post().uri("/auth/login").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(401);
    }

    @Test
    void shouldReturn401_WhenLoginUserIsNotExists() {
        var dto = new AuthRequest("Test User", "testpass1234");
        webTestClient.post().uri("/auth/login").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(401);
    }

    @Test
    void shouldReturn401_WhenUsernameIsNotCorrect() {
        userRepository.save(User.builder().username("Test User").password(passwordEncoder.encode("test123")).build());

        var dto = new AuthRequest("Test User2", "testpass1234");
        webTestClient.post().uri("/auth/login").bodyValue(dto).exchange()
                .expectStatus().isEqualTo(401);
    }
}