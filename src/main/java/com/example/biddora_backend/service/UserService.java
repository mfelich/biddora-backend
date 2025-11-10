package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.userDtos.EditUserDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface UserService {
    UserDto getUser(Long userId);
    Page<UserDto> getAllUsers(Optional<Integer> page, Optional<String> sortBy,Optional<String> username);
    UserDto editUser(Long userId, EditUserDto editUserDto) throws AccessDeniedException;
    String deleteUser(Long id) throws AccessDeniedException;
}
