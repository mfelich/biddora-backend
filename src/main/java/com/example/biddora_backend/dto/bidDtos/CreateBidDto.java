package com.example.biddora_backend.dto.bidDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBidDto {

    private Long productId;
    private Long amount;
}
