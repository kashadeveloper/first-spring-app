package com.studying.first_spring_app.dto;

public record AuthorizationResponse(String accessToken, UserDto user) {
}
