package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.auth.AuthResponse;
import com.example.biddora_backend.dto.auth.RegisterRequestDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService=authService;
    }

    //Register user
    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserDto userDto = authService.register(registerRequestDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    //Verify user
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse answer = authService.login(authRequest);

        return ResponseEntity.ok(answer);
    }

}
