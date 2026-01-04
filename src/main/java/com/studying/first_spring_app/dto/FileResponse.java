package com.studying.first_spring_app.dto;

import org.springframework.http.MediaType;

import java.io.InputStream;

public record FileResponse(InputStream stream, String fileName, MediaType contentType) {
}
