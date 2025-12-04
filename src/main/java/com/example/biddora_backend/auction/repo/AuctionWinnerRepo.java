package com.example.biddora_backend.auction.repo;

import com.example.biddora_backend.auction.entity.AuctionWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionWinnerRepo extends JpaRepository<AuctionWinner,Long> {
    Optional<AuctionWinner> findByProductId(Long productId);
}
