package com.example.biddora_backend.bid.mapper;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.username", target = "bidderUsername")
    BidDto mapToDto(Bid bid);
}
