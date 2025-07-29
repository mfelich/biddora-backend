package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.AuctionWinnerDto;
import com.example.biddora_backend.entity.AuctionWinner;
import com.example.biddora_backend.entity.Product;

public interface AuctionWinnerService {
    AuctionWinner createWinner(Product product);
    AuctionWinnerDto getAuctionWinner(Long productId);
}
