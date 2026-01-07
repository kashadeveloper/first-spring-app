package com.studying.first_spring_app.mapper;

import com.studying.first_spring_app.dto.*;
import com.studying.first_spring_app.model.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TaskMapper {
    Task toEntity(TaskDto dto);
    Task toEntity(CreateTaskDto dto);

    TaskSummaryDto toSummaryDto(Task task);
    TaskDto toDetailedDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(PatchTaskDto dto, @MappingTarget Task entity);

    List<TaskDto> toDto(List<Task> tasks);

    List<TaskSummaryDto> toSummaryDto(List<Task> tasks);
}
