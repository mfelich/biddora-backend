package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.userDtos.EditUserDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.UserAccessDeniedException;
import com.example.biddora_backend.mapper.UserMapper;
import com.example.biddora_backend.repo.UserRepo;
import com.example.biddora_backend.service.util.EntityFetcher;
import com.example.biddora_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final EntityFetcher entityFetcher;

    @Override
    public UserDto getUser(Long userId) {
        User user = entityFetcher.getUserById(userId);

        return userMapper.mapToDto(user);
    }

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

    @Override
    public UserDto editUser(Long userId, EditUserDto editUserDto) {
        User currentUser = entityFetcher.getCurrentUser();
        User user = entityFetcher.getUserById(userId);

        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(userId)){
            throw new UserAccessDeniedException("You do not have permission to edit this user.");
        }

        if (currentUser.getRole().equals(Role.ADMIN)){
            user.setRole(editUserDto.getRole());
        }
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());

        return userMapper.mapToDto(userRepo.save(user));
    }

    @Override
    public String deleteUser(Long id) {
        User user = entityFetcher.getCurrentUser();

        if (!user.getRole().equals(Role.ADMIN) && !user.getId().equals(id)) {
            throw new UserAccessDeniedException("You do not have permission to delete this user.");
        }

        userRepo.delete(entityFetcher.getUserById(id));
        return "User deleted successfully.";
    }
}
