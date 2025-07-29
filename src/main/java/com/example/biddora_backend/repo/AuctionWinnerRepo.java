package com.example.biddora_backend.repo;

import com.example.biddora_backend.entity.AuctionWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionWinnerRepo extends JpaRepository<AuctionWinner,Long> {
    AuctionWinner findByProductId(Long productId);
}
