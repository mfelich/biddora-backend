package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.auth.AuthRequest;

public interface AuthService {
    String login(AuthRequest authRequest);
}
