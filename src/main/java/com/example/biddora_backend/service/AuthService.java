package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;

public interface AuthService {
    UserDto register(User user);
    String login(AuthRequest authRequest);
}
