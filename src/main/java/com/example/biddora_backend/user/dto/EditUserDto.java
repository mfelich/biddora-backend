package com.example.biddora_backend.user.dto;

import com.example.biddora_backend.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    private String firstName;
    private String lastName;
    private Role role;
}
