package com.example.biddora_backend.rating.service;

import com.example.biddora_backend.rating.dto.CreateRatingDto;
import com.example.biddora_backend.rating.dto.RatingDto;
import com.example.biddora_backend.rating.dto.UpdateRatingDto;

import java.util.List;

public interface RatingService {
    RatingDto createRating(CreateRatingDto createRatingDto);
    void deleteRating(Long ratingId);
    RatingDto updateRating(Long ratingId, UpdateRatingDto updateRatingDto);
    RatingDto getById(Long id);
    List<RatingDto> getRatingsByUserId(Long userId);
    List<RatingDto> getProductRatings(Long productId);
}
