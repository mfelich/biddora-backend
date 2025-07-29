package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.userDtos.EditUserDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }


    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto userDto = userService.createUser(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    //Edit user
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    ResponseEntity<UserDto> editUser(@RequestBody EditUserDto editUserDto){
        UserDto userDto = userService.editUser(editUserDto);

        return ResponseEntity.ok(userDto);
    }

    //Get user by id
    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id){
        UserDto userDto = userService.getUser(id);

        return ResponseEntity.ok(userDto);
    }

    //Get all users
    @GetMapping("/all")
    ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sortBy,
                                              @RequestParam Optional<String> username){
        Page<UserDto> userDtos = userService.getAllUsers(page, sortBy, username);

        return ResponseEntity.ok(userDtos);
    }


    //Delete user by id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    ResponseEntity<String> deleteUser(@PathVariable Long id) throws AccessDeniedException {
        String answer = userService.deleteUser(id);
        return ResponseEntity.ok(answer);
    }
}
