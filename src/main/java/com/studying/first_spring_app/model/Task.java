package com.studying.first_spring_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, unique = true, nullable = false)
    private String title = "";

    @Column(length = 450, nullable = false)
    private String description = "";

    @Column(nullable = false, name = "is_completed")
    private boolean completed = false;

    @Column(name = "image_id")
    private String imageId = "";
}