package com.studying.first_spring_app.repository;

import com.studying.first_spring_app.model.Task;
import com.studying.first_spring_app.model.TaskPriority;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class TaskSpecification {
    public static Specification<Task> byUser(UUID userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Task> all() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Task> hasTitle(String title) {
        return (root, criteriaQuery, builder)
                -> builder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Task> hasCompletedStatus(boolean completed) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("completed"), completed);
    }

    public static Specification<Task> hasPriority(String priority) throws ResponseStatusException {
        TaskPriority tp;
        try {
            tp = TaskPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Invalid priority");
        }
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), tp);
    }

    public static Specification<Task> orderByPriority(boolean desc) {
        return (root, query, cb) -> {
            var caseExpr = cb.selectCase();
            caseExpr.when(cb.equal(root.get("priority"), TaskPriority.HIGH), 3);
            caseExpr.when(cb.equal(root.get("priority"), TaskPriority.MEDIUM), 2);
            caseExpr.when(cb.equal(root.get("priority"), TaskPriority.LOW), 1);
            caseExpr.otherwise(0);

            if (desc) {
                query.orderBy(cb.desc(caseExpr));
            } else {
                query.orderBy(cb.asc(caseExpr));
            }

            return cb.conjunction();
        };
    }

}
