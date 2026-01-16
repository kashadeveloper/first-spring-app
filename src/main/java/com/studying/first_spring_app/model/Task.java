package com.studying.first_spring_app.model;

import com.studying.first_spring_app.model.listeners.TaskListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@EntityListeners(TaskListener.class)
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
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}