package com.example.biddora_backend.bid.controller;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.dto.CreateBidDto;
import com.example.biddora_backend.bid.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    ResponseEntity<BidDto> placeBid(@Valid @RequestBody CreateBidDto createBidDto) {
        BidDto bidDto = bidService.placeBid(createBidDto);
        return ResponseEntity.ok(bidDto);
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<Page<BidDto>> getBidsForProduct(@PathVariable Long productId,
                                                   @RequestParam Optional<Integer> page){

        return ResponseEntity.ok(bidService.getBidsByProductId(productId,page));
    }
}
