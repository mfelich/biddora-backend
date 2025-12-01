package com.example.biddora_backend.auction.service.impl;

import com.example.biddora_backend.auction.service.AuctionWinnerService;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.product.repo.ProductRepo;
import com.example.biddora_backend.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionStatusScheduler {

    private final ProductRepo productRepo;
    private final ProductService productService;
    private final AuctionWinnerService auctionWinnerService;

    @Scheduled(cron = "1 * * * * *")
    public void updateProductStatus(){
        openScheduledAuctions();
        closeExpiredAuctions();
    }


    @Transactional
    private void openScheduledAuctions() {
        List<Product> toOpen = productRepo.findByProductStatusAndStartTimeLessThanEqual(ProductStatus.SCHEDULED, LocalDateTime.now());
        toOpen.forEach(p -> p.setProductStatus(ProductStatus.OPEN));
        productRepo.saveAll(toOpen);
    }


    @Transactional
    private void closeExpiredAuctions() {
        List<Product> toClose = productRepo.findByProductStatusAndEndTimeLessThanEqual(ProductStatus.OPEN, LocalDateTime.now());
        List<Product> allToSave = new ArrayList<>();

        toClose.forEach(p -> {
            try {
                p.setAuctionWinner(auctionWinnerService.createWinner(p));
                p.setProductStatus(ProductStatus.CLOSED);
                allToSave.add(p);
            } catch (Exception e) {
                System.err.println("Greška kod kreiranja pobjednika za proizvod id=" + p.getId() + ": " + e.getMessage());
            }
        });

        productRepo.saveAll(allToSave);
    }

}
