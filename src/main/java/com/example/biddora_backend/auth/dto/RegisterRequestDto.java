package com.example.biddora_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Username can not be blank!")
    @Size(min = 5,max = 15)
    private String username;

    @NotBlank(message = "First name can not be blank!")
    private String firstName;

    @NotBlank(message = "Last name can not be blank!")
    private String lastName;

    @Size(min = 5)
    private String password;

    @Email
    private String email;
}
