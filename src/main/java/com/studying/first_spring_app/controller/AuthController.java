package com.studying.first_spring_app.controller;

import com.studying.first_spring_app.dto.AuthRequest;
import com.studying.first_spring_app.dto.AuthorizationResponse;
import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.mapper.UserMapper;
import com.studying.first_spring_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("register")
    public AuthorizationResponse register(@RequestBody @Valid CreateUserDto dto) {
        return authService.register(dto);
    }

    @PostMapping("login")
    public AuthorizationResponse login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }
}
