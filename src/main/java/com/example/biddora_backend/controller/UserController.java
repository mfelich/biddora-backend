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

    public UserController(UserService userService) {
        this.userService=userService;
    }

    @PutMapping("/{userId}")
    ResponseEntity<UserDto> editUser(@PathVariable("userId") Long userId,
                                     @RequestBody EditUserDto editUserDto) {
        UserDto userDto = userService.editUser(userId,editUserDto);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id){
        UserDto userDto = userService.getUser(id);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sortBy,
                                              @RequestParam Optional<String> username){
        return ResponseEntity.ok(userService.getAllUsers(page, sortBy, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String answer = userService.deleteUser(id);
        return ResponseEntity.ok(answer);
    }
}
