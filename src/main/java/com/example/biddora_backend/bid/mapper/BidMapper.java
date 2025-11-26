package com.example.biddora_backend.bid.mapper;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.entity.Bid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BidMapper {
    BidDto mapToDto(Bid bid);
}
