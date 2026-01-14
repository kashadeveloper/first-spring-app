package com.studying.first_spring_app.service;

import com.studying.first_spring_app.dto.AuthRequest;
import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.dto.RefreshTokenAuthToken;
import com.studying.first_spring_app.dto.TokensResponse;
import com.studying.first_spring_app.mapper.UserMapper;
import com.studying.first_spring_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public TokensResponse register(CreateUserDto dto) {
        var user = User.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        user = userService.create(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new TokensResponse(accessToken, refreshToken);
    }

    public TokensResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = userService.findByUsername(request.username());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new TokensResponse(jwtToken, refreshToken);
    }

    public TokensResponse refresh(String refreshToken) {
        authenticationManager.authenticate(new RefreshTokenAuthToken(refreshToken));
        var user = userService.findByUsername(jwtService.extractUsername(refreshToken));
        var jwtToken = jwtService.generateToken(user);
        var generatedRefreshToken = jwtService.generateRefreshToken(user);
        return new TokensResponse(jwtToken, generatedRefreshToken);
    }
}
