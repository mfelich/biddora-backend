package com.example.biddora_backend.user.service.impl;

import com.example.biddora_backend.user.dto.EditUserDto;
import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.user.enums.Role;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.common.exception.UserAccessDeniedException;
import com.example.biddora_backend.user.mapper.UserMapper;
import com.example.biddora_backend.user.repo.UserRepo;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.user.service.UserService;
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
