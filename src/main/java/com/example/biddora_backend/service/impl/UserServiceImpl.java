package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.userDtos.EditUserDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.mapper.UserMapper;
import com.example.biddora_backend.repo.UserRepo;
import com.example.biddora_backend.service.util.EntityFetcher;
import com.example.biddora_backend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserMapper userMapper;
    private EntityFetcher entityFetcher;


    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, EntityFetcher entityFetcher) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.entityFetcher = entityFetcher;
    }

    //Get user by id
    @Override
    public UserDto getUser(Long userId) {
        User user = entityFetcher.getUserById(userId);

        return userMapper.mapToDto(user);
    }

    //Get all users
    @Override
    public Page<UserDto> getAllUsers(Optional<Integer> page, Optional<String> sortBy, Optional<String> username) {
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                12,
                Sort.Direction.ASC,
                sortBy.orElse("username")
        );

        Page<User> userDtos;

        if (username.isPresent()) {
            userDtos = userRepo.findByUsernameContainingIgnoreCase(username.get(), pageRequest);
        } else {
            userDtos = userRepo.findAll(pageRequest);
        }

        return userDtos.map(userMapper::mapToDto);
    }

    //Edit user
    @Override
    public UserDto editUser(Long userId, EditUserDto editUserDto) throws AccessDeniedException {
        User currentUser = entityFetcher.getCurrentUser();
        User user = entityFetcher.getUserById(userId);

        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(userId)){
            throw new AccessDeniedException("You do not have permission to edit this user.");
        }

        if (currentUser.getRole().equals(Role.ADMIN)){
            user.setRole(editUserDto.getRole());
        }
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());

        return userMapper.mapToDto(userRepo.save(user));
    }

    //Delete user
    @Override
    public String deleteUser(Long id) throws AccessDeniedException {
        User user = entityFetcher.getCurrentUser();

        if (!user.getRole().equals(Role.ADMIN) && !user.getId().equals(id)) {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }

        userRepo.delete(entityFetcher.getUserById(id));
        return "User deleted successfully!";
    }
}
