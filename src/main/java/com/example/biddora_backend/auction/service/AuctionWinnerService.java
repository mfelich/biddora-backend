package com.example.biddora_backend.auction.service;

import com.example.biddora_backend.auction.dto.AuctionWinnerDto;
import com.example.biddora_backend.auction.entity.AuctionWinner;
import com.example.biddora_backend.product.entity.Product;

public interface AuctionWinnerService {
    AuctionWinner createWinner(Product product);
    AuctionWinnerDto getAuctionWinner(Long productId);
}
