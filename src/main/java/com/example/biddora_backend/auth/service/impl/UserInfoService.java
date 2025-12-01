package com.example.biddora_backend.auth.service.impl;

import com.example.biddora_backend.auth.model.UserInfoDetails;
import com.example.biddora_backend.common.exception.UserNotFoundException;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {


    @Autowired
    private final UserRepo userRepo;

    public UserInfoService(UserRepo userRepo) {
        this.userRepo=userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        return new UserInfoDetails(user);
    }
}
