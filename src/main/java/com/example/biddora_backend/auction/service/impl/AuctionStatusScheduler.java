package com.example.biddora_backend.auction.service.impl;

import com.example.biddora_backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionStatusScheduler {

    private final ProductService productService;

    @Scheduled(cron = "1 * * * * *")
    public void updateProductStatus(){
        productService.openScheduledAuctions();
        productService.closeExpiredAuctions();
    }

}
