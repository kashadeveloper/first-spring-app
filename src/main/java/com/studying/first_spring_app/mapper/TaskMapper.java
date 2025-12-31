package com.studying.first_spring_app.mapper;

import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.model.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "description", defaultValue = "")
    @Mapping(target = "completed", defaultValue = "false")
    Task toEntity(TaskDto dto);

    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(PatchTaskDto dto, @MappingTarget Task entity);

    List<TaskDto> toDto(List<Task> tasks);
}
