package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.service.AuthService;
import com.example.biddora_backend.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
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
