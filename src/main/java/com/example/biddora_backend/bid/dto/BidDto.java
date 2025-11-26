package com.example.biddora_backend.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidDto {
    private Long id;
    private Long productId;
    private Long amount;
    private String bidderUsername;
    private LocalDateTime timestamp;
}
