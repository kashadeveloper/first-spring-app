package com.studying.first_spring_app.config;

import com.studying.first_spring_app.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class SecurityConfigTest extends BaseIT {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldPermitAll_forPrometheusEndpoint() {
        webTestClient.get().uri("/actuator/prometheus").exchange().expectStatus().isOk();
    }

    @Test
    void shouldDenyAll_forActuatorHealth() {
        webTestClient.get().uri("/actuator/health").exchange().expectStatus().isForbidden();
    }

    @Test
    void shouldShow403_forActuatorNotHealth() {
        webTestClient.get().uri("/actuator/env").exchange().expectStatus().isForbidden();
    }

    @Test
    void shouldPermitAll_forSwagger() {
        webTestClient.get().uri("/api-docs").exchange().expectStatus().isOk();
        webTestClient.get().uri("/api-docs/swagger").exchange().expectStatus().isFound();
    }

    @Test
    void shouldReturn400_WhenOriginIsEmptyOrCorrect() {
        webTestClient.post().uri("/auth/login").exchange().expectStatus().isBadRequest();
    }

    @Test
    void shouldReturn403_WhenOriginIsInvalid() {
        webTestClient.post().uri("/auth/login").header("Origin", "https://mytestwebsite.com").exchange().expectStatus().isForbidden().expectBody(String.class);
    }
}
