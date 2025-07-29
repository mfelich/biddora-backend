package com.example.biddora_backend.dto.productDtos;

import com.example.biddora_backend.dto.userDtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private String name;
    private Long startingPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UserDto userDto;
}
