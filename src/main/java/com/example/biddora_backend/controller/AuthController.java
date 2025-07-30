package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService=authService;
    }

    //Register new user
    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto userDto = authService.register(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    //Verify user
    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String answer = authService.login(authRequest);

        return ResponseEntity.ok(answer);
    }

}
