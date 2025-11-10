package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.AuctionWinnerDto;
import com.example.biddora_backend.entity.AuctionWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionWinnerMapper {

    private UserMapper userMapper;
    private ProductMapper productMapper;

    @Autowired
    public AuctionWinnerMapper(UserMapper userMapper,ProductMapper productMapper) {
        this.userMapper=userMapper;
        this.productMapper=productMapper;
    }

    public AuctionWinnerDto mapToDto(AuctionWinner auctionWinner){
        return new AuctionWinnerDto(
                auctionWinner.getId(),
                auctionWinner.getAmount(),
                userMapper.mapToDto(auctionWinner.getUser()),
                productMapper.mapToDto(auctionWinner.getProduct())
        );
    }
}
