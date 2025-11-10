package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.auth.AuthResponse;
import com.example.biddora_backend.dto.auth.RegisterRequestDto;
import com.example.biddora_backend.dto.userDtos.UserDto;

public interface AuthService {
    UserDto register(RegisterRequestDto registerRequestDto);
    AuthResponse login(AuthRequest authRequest);
}
