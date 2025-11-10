package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.repo.ProductRepo;
import com.example.biddora_backend.service.AuctionWinnerService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionStatusScheduler {

    private final ProductRepo productRepo;
    private final AuctionWinnerService auctionWinnerService;

    public AuctionStatusScheduler(ProductRepo productRepo,AuctionWinnerService auctionWinnerService){
        this.productRepo=productRepo;
        this.auctionWinnerService=auctionWinnerService;
    }

    @Scheduled(cron = "1 * * * * *")
    @Transactional
    public void updateProductStatus(){

        LocalDateTime now = LocalDateTime.now();

        // Open auctions
        List<Product> toOpen = productRepo.findByProductStatusAndStartTimeLessThanEqual(ProductStatus.SCHEDULED, now);
        toOpen.forEach(p -> p.setProductStatus(ProductStatus.OPEN));

        // Close auctions
        List<Product> toClose = productRepo.findByProductStatusAndEndTimeLessThanEqual(ProductStatus.OPEN, now);
        toClose.forEach(p -> {
            p.setProductStatus(ProductStatus.CLOSED);
            p.setAuctionWinner(auctionWinnerService.createWinner(p));
        });

        // Save
        productRepo.saveAll(toOpen);
        productRepo.saveAll(toClose);
    }
}
