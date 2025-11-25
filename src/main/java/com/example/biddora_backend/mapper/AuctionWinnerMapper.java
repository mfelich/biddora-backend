package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.AuctionWinnerDto;
import com.example.biddora_backend.entity.AuctionWinner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuctionWinnerMapper {
    AuctionWinnerDto mapToDto(AuctionWinner auctionWinner);
}
