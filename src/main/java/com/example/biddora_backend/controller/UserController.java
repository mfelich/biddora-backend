package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.userDtos.EditUserDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import com.example.biddora_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //Edit user
    @PutMapping("/{userId}")
    ResponseEntity<UserDto> editUser(@PathVariable("userId") Long userId,
                                     @RequestBody EditUserDto editUserDto)throws AccessDeniedException{
        UserDto userDto = userService.editUser(userId,editUserDto);

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
        return ResponseEntity.ok(userService.getAllUsers(page, sortBy, username));
    }

    //Delete user by id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    ResponseEntity<String> deleteUser(@PathVariable Long id) throws AccessDeniedException {
        String answer = userService.deleteUser(id);
        return ResponseEntity.ok(answer);
    }
}
