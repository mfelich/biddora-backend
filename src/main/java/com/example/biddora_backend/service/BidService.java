package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;

import java.nio.file.AccessDeniedException;

public interface BidService {
    BidDto placeBid(CreateBidDto createBidDto) throws AccessDeniedException;
}
