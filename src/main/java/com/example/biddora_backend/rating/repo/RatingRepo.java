package com.example.biddora_backend.rating.repo;

import com.example.biddora_backend.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByUserId(Long userId);
    List<Rating> findByProductId(Long productId);
}
