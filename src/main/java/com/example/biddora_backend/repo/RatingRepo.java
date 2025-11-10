package com.example.biddora_backend.repo;

import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByUserId(Long userId);
    List<Rating> findByProductId(Long productId);
}
