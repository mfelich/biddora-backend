package com.example.biddora_backend.user.mapper;

import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapToDto(User user);
}
