package com.studying.first_spring_app;

import com.studying.first_spring_app.dto.AuthRequest;
import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.repository.UserRepository;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class AuthControllerTest extends BaseIT {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateUser()
    {
        var request = new CreateUserDto("Test", "hahah");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .log().all()
        .when()
                .post("/auth/register").then()
                .statusCode(200);

        var user = userRepository.findByUsername("Test");

        assertThat(user).isPresent();
        assertThat(user.get().getPassword()).isNotEqualTo("hahah");
        assertThat(user.get().getUsername()).isEqualTo("Test");
    }

    @Test
    public void shouldLoginUser() {
        var request = new AuthRequest("Test", "hahah");

        given().contentType(ContentType.JSON).body(request)
                .log().all()
                .when()
                .post("/auth/login").then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .cookie("rf", notNullValue())
                .body("refreshToken", notNullValue());

    }
}
