package com.example.biddora_backend.bid.repo;

import com.example.biddora_backend.bid.entity.Bid;
import com.example.biddora_backend.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepo extends JpaRepository<Bid,Long> {
    Optional<Bid> findTopByProductOrderByAmountDesc(Product product);
    Page<Bid> findByProductIdOrderByAmountDesc(Long productId, Pageable pageable);
}
