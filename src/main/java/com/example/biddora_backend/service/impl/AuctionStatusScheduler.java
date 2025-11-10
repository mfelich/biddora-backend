package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.repo.ProductRepo;
import com.example.biddora_backend.service.AuctionWinnerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(fixedRate = 60_000)
    public void updateProductStatus(){

        LocalDateTime now = LocalDateTime.now();

        List<Product> toOpen = productRepo.findByProductStatusAndStartTimeLessThanEqual(ProductStatus.SCHEDULED, now);
        toOpen.forEach(p -> p.setProductStatus(ProductStatus.OPEN));

        //Zatvori proizvode koji su "open" i endTime <= now i kreiraj AuctionWinner preko servisa
        List<Product> toClose = productRepo.findByProductStatusAndEndTimeLessThanEqual(ProductStatus.OPEN, now);
        toClose.forEach(p -> p.setProductStatus(ProductStatus.CLOSED));
        productRepo.saveAll(toClose);
        toClose.forEach(p -> p.setAuctionWinner(auctionWinnerService.createWinner(p)));
        productRepo.saveAll(toOpen);

    }
}
