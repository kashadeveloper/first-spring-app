package com.studying.first_spring_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokensResponse(String accessToken, String refreshToken) {
}
