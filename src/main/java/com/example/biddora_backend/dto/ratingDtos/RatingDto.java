package com.example.biddora_backend.dto.ratingDtos;

import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.dto.userDtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    private Long id;
    private UserDto user;
    private ProductDto product;
    private String comment;
    private Integer ratingStars;
    private LocalDateTime date;
}
