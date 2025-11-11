package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;
import com.example.biddora_backend.service.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api/bid")
@CrossOrigin(value = "http://localhost:5174")
public class BidController {

    @Autowired
    public BidService bidService;

    @PostMapping
    ResponseEntity<BidDto> placeBid(@Valid @RequestBody CreateBidDto createBidDto) throws AccessDeniedException {
        BidDto bidDto = bidService.placeBid(createBidDto);
        return ResponseEntity.ok(bidDto);
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<Page<BidDto>> getBidsForProduct(@PathVariable Long productId,
                                           @RequestParam Optional<Integer> page){

        return ResponseEntity.ok(bidService.getBidsByProductId(productId,page));
    }
}
