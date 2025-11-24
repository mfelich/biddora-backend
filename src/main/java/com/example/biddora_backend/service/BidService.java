package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface BidService {
    BidDto placeBid(CreateBidDto createBidDto);
    Page<BidDto> getBidsByProductId(Long productId, Optional<Integer> page);
}
