package com.studying.first_spring_app.mapper;

import com.studying.first_spring_app.dto.CreateTaskDto;
import com.studying.first_spring_app.dto.PatchTaskDto;
import com.studying.first_spring_app.dto.TaskDto;
import com.studying.first_spring_app.model.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "description", defaultValue = "")
    @Mapping(target = "completed", defaultValue = "false")
    @Mapping(target = "imageId", source = "image_id", defaultValue = "")
    Task toEntity(TaskDto dto);

    @Mapping(target = "description", defaultValue = "")
    @Mapping(target = "completed", defaultValue = "false")
    Task toEntity(CreateTaskDto dto);

    @Mapping(target = "image_id", source = "imageId")
    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(PatchTaskDto dto, @MappingTarget Task entity);

    List<TaskDto> toDto(List<Task> tasks);
}
