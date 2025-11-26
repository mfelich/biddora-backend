package com.example.biddora_backend.bid.service;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.dto.CreateBidDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BidService {
    BidDto placeBid(CreateBidDto createBidDto);
    Page<BidDto> getBidsByProductId(Long productId, Optional<Integer> page);
}
