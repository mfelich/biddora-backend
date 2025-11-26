package com.example.biddora_backend.auction.mapper;

import com.example.biddora_backend.auction.dto.AuctionWinnerDto;
import com.example.biddora_backend.auction.entity.AuctionWinner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuctionWinnerMapper {
    AuctionWinnerDto mapToDto(AuctionWinner auctionWinner);
}
