package com.studying.first_spring_app.repository;

import com.studying.first_spring_app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, UUID id);

    @Modifying
    @Query("DELETE FROM Task WHERE id IN :ids")
    void deleteAllByIds(@Param("ids") List<UUID> ids);
}