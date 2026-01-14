package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.AuthRequest;
import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.dto.TokensResponse;
import com.studying.first_spring_app.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<TokensResponse> register(@RequestBody @Valid CreateUserDto dto, HttpServletResponse response) {
        var result = authService.register(dto);
        return getAuthorizationResponseResponseEntity(response, result);
    }

    @PostMapping("login")
    public ResponseEntity<TokensResponse> login(@RequestBody @Valid AuthRequest request, HttpServletResponse response) {
        var result = authService.login(request);
        return getAuthorizationResponseResponseEntity(response, result);
    }

    @PostMapping("new-session")
    public ResponseEntity<TokensResponse> newSession(@CookieValue("rf") String refreshToken, HttpServletResponse response) {
        if(refreshToken == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401));
        }
        var result = authService.refresh(refreshToken);
        return getAuthorizationResponseResponseEntity(response, result);
    }

    @NotNull
    private ResponseEntity<TokensResponse> getAuthorizationResponseResponseEntity(HttpServletResponse response, TokensResponse result) {
        var cookie = new Cookie("rf", result.refreshToken());

        cookie.setMaxAge(5 * 24 * 60 * 60);
        cookie.setPath("/auth/new-session");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(result);
    }
}
