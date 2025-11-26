package com.example.biddora_backend.auction.controller;

import com.example.biddora_backend.auction.dto.AuctionWinnerDto;
import com.example.biddora_backend.auction.service.AuctionWinnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auction-winner")
@RequiredArgsConstructor
public class AuctionWinnerController {

    private final AuctionWinnerService auctionWinnerService;

    @GetMapping("/product/{productId}")
    ResponseEntity<AuctionWinnerDto> getAuctionWinner(@PathVariable Long productId){
        AuctionWinnerDto auctionWinnerDto = auctionWinnerService.getAuctionWinner(productId);

        return ResponseEntity.ok(auctionWinnerDto);
    }
}
