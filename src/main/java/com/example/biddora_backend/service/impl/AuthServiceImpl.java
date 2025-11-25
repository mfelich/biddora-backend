package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.auth.AuthRequest;
import com.example.biddora_backend.dto.auth.AuthResponse;
import com.example.biddora_backend.dto.auth.RegisterRequestDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.ResourceAlreadyExistsException;
import com.example.biddora_backend.mapper.UserMapper;
import com.example.biddora_backend.repo.UserRepo;
import com.example.biddora_backend.service.AuthService;
import com.example.biddora_backend.service.security.JWTService;
import com.example.biddora_backend.service.util.EntityFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final EntityFetcher entityFetcher;

    @Override
    public UserDto register(RegisterRequestDto registerRequestDto) {

        if (entityFetcher.existsByEmail(registerRequestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exist!");
        }

        if (entityFetcher.existsByUsername(registerRequestDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exist!");
        }

        User newUser = new User();
        newUser.setUsername(registerRequestDto.getUsername());
        newUser.setFirstName(registerRequestDto.getFirstName());
        newUser.setLastName(registerRequestDto.getLastName());
        newUser.setEmail(registerRequestDto.getEmail());
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setPassword(encoder.encode(registerRequestDto.getPassword()));
        newUser.setRole(Role.USER);

        User savedUser = userRepo.save(newUser);

        return userMapper.mapToDto(savedUser);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        User user = entityFetcher.findUserByUsername(authRequest.getUsername());

        String token = jwtService.generateToken(user.getUsername());

        String role = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(userMapper.mapToDto(user));

        return authResponse;
    }
}
