package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.AuctionWinnerDto;
import com.example.biddora_backend.entity.AuctionWinner;
import com.example.biddora_backend.entity.Bid;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.exception.AuctionNotEndedException;
import com.example.biddora_backend.exception.NoBidsForProductException;
import com.example.biddora_backend.mapper.AuctionWinnerMapper;
import com.example.biddora_backend.repo.AuctionWinnerRepo;
import com.example.biddora_backend.repo.BidRepo;
import com.example.biddora_backend.service.AuctionWinnerService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuctionWinnerServiceImpl implements AuctionWinnerService {

    private AuctionWinnerRepo auctionWinnerRepo;
    private BidRepo bidRepo;
    private EntityFetcher entityFetcher;
    private AuctionWinnerMapper auctionWinnerMapper;

    public AuctionWinnerServiceImpl(AuctionWinnerRepo auctionWinnerRepo, BidRepo bidRepo, AuctionWinnerMapper auctionWinnerMapper, EntityFetcher entityFetcher) {
        this.auctionWinnerRepo = auctionWinnerRepo;
        this.bidRepo = bidRepo;
        this.auctionWinnerMapper = auctionWinnerMapper;
        this.entityFetcher = entityFetcher;
    }

    @Override
    @Transactional
    public AuctionWinner createWinner(Product product) {

        Optional<Bid> winningBidOpt = bidRepo.findTopByProductOrderByAmountDesc(product);

        if (winningBidOpt.isEmpty()) {
            throw new NoBidsForProductException("There are no bids for the product with ID: " + product.getId());
        }

        Bid winningBid = winningBidOpt.get();

        //creating winner
        AuctionWinner auctionWinner = new AuctionWinner();
        auctionWinner.setUser(winningBid.getUser());
        auctionWinner.setProduct(product);
        auctionWinner.setAmount(winningBid.getAmount());

        //saved winner
        AuctionWinner savedAuctionWinner = auctionWinnerRepo.save(auctionWinner);

        return savedAuctionWinner;
    }

    @Override
    public AuctionWinnerDto getAuctionWinner(Long productId) {

        Product product = entityFetcher.getProductById(productId);

        if (!product.getProductStatus().equals(ProductStatus.CLOSED)) {
            throw new AuctionNotEndedException("The auction for this product has not ended yet.");
        }

        AuctionWinner auctionWinner = auctionWinnerRepo.findByProductId(productId);

        return auctionWinnerMapper.mapToDto(auctionWinner);
    }
}
