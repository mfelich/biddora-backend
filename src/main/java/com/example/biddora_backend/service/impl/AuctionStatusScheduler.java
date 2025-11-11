package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionStatusScheduler {

    private final ProductService productService;

    public AuctionStatusScheduler(ProductService productService){
        this.productService=productService;
    }

    @Scheduled(cron = "1 * * * * *")
    public void updateProductStatus(){
        productService.openScheduledAuctions();
        productService.closeExpiredAuctions();
    }

}
