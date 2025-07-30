package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.mapper.UserMapper;
import com.example.biddora_backend.repo.UserRepo;
import com.example.biddora_backend.service.AuthService;
import com.example.biddora_backend.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private UserRepo userRepo;
    private UserMapper userMapper;
    private JWTService jwtService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,UserRepo userRepo, UserMapper userMapper, JWTService jwtService) {
        this.authenticationManager=authenticationManager;
        this.userRepo=userRepo;
        this.userMapper=userMapper;
        this.jwtService=jwtService;
    }

    @Override
    public UserDto register(User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setUsername(user.getUsername());

        User savedUser = userRepo.save(newUser);

        return userMapper.mapToDto(savedUser);
    }

    @Override
    public String login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        }

        return "Bad creditals!";
    }
    }
