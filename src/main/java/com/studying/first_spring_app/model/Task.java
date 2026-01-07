package com.studying.first_spring_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @Builder.Default
    @Column(length = 100, unique = true, nullable = false)
    private String title = "";

    @Builder.Default
    @Column(length = 450, nullable = false)
    private String description = "";

    @Builder.Default
    @Column(nullable = false, name = "is_completed")
    private boolean completed = false;

    @Builder.Default
    @Column(name = "image_id")
    private String imageId = "";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}