package com.example.biddora_backend.favorite.dto;

import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {

    private Long id;
    private UserDto user;
    private ProductDto product;
}
