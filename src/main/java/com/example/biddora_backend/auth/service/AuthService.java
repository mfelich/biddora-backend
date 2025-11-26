package com.example.biddora_backend.auth.service;

import com.example.biddora_backend.auth.dto.AuthRequest;
import com.example.biddora_backend.auth.dto.AuthResponse;
import com.example.biddora_backend.auth.dto.RegisterRequestDto;
import com.example.biddora_backend.user.dto.UserDto;

public interface AuthService {
    UserDto register(RegisterRequestDto registerRequestDto);
    AuthResponse login(AuthRequest authRequest);
}
