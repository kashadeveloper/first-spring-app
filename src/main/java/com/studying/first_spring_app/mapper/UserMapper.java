package com.studying.first_spring_app.mapper;

import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.dto.UserDto;
import com.studying.first_spring_app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    User toEntity(CreateUserDto dto);
}
