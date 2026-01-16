package com.studying.first_spring_app.repository;

import com.studying.first_spring_app.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, UUID id);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.id IN :ids AND t.user.id = :user_id")
    void deleteAllByIds(@Param("ids") List<UUID> ids, @Param("user_id") UUID user_id);
}