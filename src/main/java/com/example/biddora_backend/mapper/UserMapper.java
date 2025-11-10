package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToDto(User user) {
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getRegistrationDate(),
                user.getEmail());
    }
}
