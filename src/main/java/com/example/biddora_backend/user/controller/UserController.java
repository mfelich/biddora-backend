package com.example.biddora_backend.user.controller;

import com.example.biddora_backend.user.dto.EditUserDto;
import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
