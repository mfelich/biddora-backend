package com.example.biddora_backend.user.service;

import com.example.biddora_backend.user.dto.EditUserDto;
import com.example.biddora_backend.user.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    UserDto getUser(Long userId);
    Page<UserDto> getAllUsers(Optional<Integer> page, Optional<String> sortBy,Optional<String> username);
    UserDto editUser(Long userId, EditUserDto editUserDto);
    String deleteUser(Long id);
}
