package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.UserDto;
import com.example.biddora_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToDto(User user) {
        return new UserDto(user.getUsername(), user.getEmail());
    }
}
