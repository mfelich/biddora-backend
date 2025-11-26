package com.example.biddora_backend.auction.dto;

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
public class AuctionWinnerDto {
    private Long id;
    private Long amount;
    private UserDto user;
    private ProductDto productDto;
}
