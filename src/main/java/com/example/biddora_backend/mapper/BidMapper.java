package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.entity.Bid;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {

    public BidDto mapToDto(Bid bid){
        return new BidDto(bid.getId(),bid.getProduct().getId(), bid.getAmount(), bid.getUser().getUsername(),bid.getTimestamp());
    }
}
