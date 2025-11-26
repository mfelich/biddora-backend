package com.example.biddora_backend.auth.controller;

import com.example.biddora_backend.auth.dto.AuthRequest;
import com.example.biddora_backend.auth.dto.AuthResponse;
import com.example.biddora_backend.auth.dto.RegisterRequestDto;
import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserDto userDto = authService.register(registerRequestDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse answer = authService.login(authRequest);

        return ResponseEntity.ok(answer);
    }

}
