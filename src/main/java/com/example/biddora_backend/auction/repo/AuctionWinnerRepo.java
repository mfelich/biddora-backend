package com.example.biddora_backend.auction.repo;

import com.example.biddora_backend.auction.entity.AuctionWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionWinnerRepo extends JpaRepository<AuctionWinner,Long> {
    AuctionWinner findByProductId(Long productId);
}
