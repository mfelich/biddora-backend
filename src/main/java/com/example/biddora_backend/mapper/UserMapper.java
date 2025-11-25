package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapToDto(User user);
}
