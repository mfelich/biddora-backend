package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.AuctionWinnerDto;
import com.example.biddora_backend.service.AuctionWinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auction-winner")
public class AuctionWinnerController {

    private AuctionWinnerService auctionWinnerService;

    @Autowired
    public AuctionWinnerController(AuctionWinnerService auctionWinnerService) {
        this.auctionWinnerService=auctionWinnerService;
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<AuctionWinnerDto> getAuctionWinner(@PathVariable Long productId){
        AuctionWinnerDto auctionWinnerDto = auctionWinnerService.getAuctionWinner(productId);

        return ResponseEntity.ok(auctionWinnerDto);
    }
}
