package com.example.biddora_backend.dto.userDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
