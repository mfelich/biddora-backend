package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;
import com.example.biddora_backend.entity.Bid;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.mapper.BidMapper;
import com.example.biddora_backend.repo.BidRepo;
import com.example.biddora_backend.service.BidService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepo bidRepo;
    private final BidMapper bidMapper;
    private final EntityFetcher entityFetcher;

    @Autowired
    public BidServiceImpl(BidRepo bidRepo,BidMapper bidMapper,EntityFetcher entityFetcher){
        this.bidRepo=bidRepo;
        this.bidMapper=bidMapper;
        this.entityFetcher=entityFetcher;
    }


    @Override
    @Transactional
    public BidDto placeBid(CreateBidDto createBidDto) throws AccessDeniedException {

        Product product = entityFetcher.getProductById(createBidDto.getProductId());
        User user = entityFetcher.getCurrentUser();

        Long highest = bidRepo.findTopByProductOrderByAmountDesc(product)
                .map(Bid::getAmount)
                .orElse(product.getStartingPrice());

        if (product.getProductStatus() != ProductStatus.OPEN) {
            throw new IllegalStateException("Bidding is not allowed. Auction is not active.");
        }

        if (product.getUser().equals(user)){
            throw new AccessDeniedException("You cannot bid on your own product.");
        }

        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            throw new IllegalStateException("Auction has ended.");
        }

        if (createBidDto.getAmount() <= highest) {
            throw new IllegalArgumentException("Bid must be higher than current highest bid.");
        }

        //Creating bid
        Bid bid = new Bid();
        bid.setAmount(Long.valueOf(createBidDto.getAmount()));
        bid.setUser(user);
        bid.setProduct(product);
        bid.setTimestamp(LocalDateTime.now());

        Bid savedBid = bidRepo.save(bid);

        return bidMapper.mapToDto(savedBid);
    }
}
