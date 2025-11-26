package com.example.biddora_backend.rating.dto;

import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.user.dto.UserDto;
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
    private LocalDateTime ratingDate;
}
