package com.example.biddora_backend.product.dto;

import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.product.enums.ProductStatus;
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

    private Long id;
    private String name;
    private Long startingPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private LocalDateTime createdAt;
    private ProductStatus productStatus;
    private UserDto user;
}
