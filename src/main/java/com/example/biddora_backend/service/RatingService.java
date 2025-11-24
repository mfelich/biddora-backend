package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.ratingDtos.CreateRatingDto;
import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.dto.ratingDtos.UpdateRatingDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface RatingService {
    RatingDto createRating(CreateRatingDto createRatingDto);
    void deleteRating(Long ratingId);
    RatingDto updateRating(Long ratingId, UpdateRatingDto updateRatingDto);
    RatingDto getById(Long id);
    List<RatingDto> getRatingsByUserId(Long userId);
    List<RatingDto> getProductRatings(Long productId);
}
