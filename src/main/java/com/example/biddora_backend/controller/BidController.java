package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;
import com.example.biddora_backend.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/bid")
public class BidController {


    public BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService=bidService;
    }

    @PostMapping
    ResponseEntity<BidDto> placeBid(@RequestBody CreateBidDto createBidDto) throws AccessDeniedException {
        BidDto bidDto = bidService.placeBid(createBidDto);

        return ResponseEntity.ok(bidDto);
    }
}
