package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.entity.Bid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BidMapper {
    BidDto mapToDto(Bid bid);
}
