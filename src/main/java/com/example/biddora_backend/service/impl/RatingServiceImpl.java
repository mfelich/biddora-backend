package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.ratingDtos.CreateRatingDto;
import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.dto.ratingDtos.UpdateRatingDto;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.Rating;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.RatingAccessDeniedException;
import com.example.biddora_backend.exception.ResourceNotFoundException;
import com.example.biddora_backend.mapper.RatingMapper;
import com.example.biddora_backend.repo.RatingRepo;
import com.example.biddora_backend.service.RatingService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepo ratingRepo;
    private final EntityFetcher entityFetcher;
    private final RatingMapper ratingMapper;

    @Override
    public RatingDto createRating(CreateRatingDto createRatingDto) {
        User user = entityFetcher.getCurrentUser();
        Product product = entityFetcher.getProductById(createRatingDto.getProductId());

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRatingDate(LocalDateTime.now());
        rating.setComment(createRatingDto.getComment());
        rating.setRatingStars(createRatingDto.getRatingStars());

        Rating savedRating = ratingRepo.save(rating);

        return ratingMapper.mapToDto(savedRating);
    }


    @Override
    @Transactional
    public void deleteRating(Long ratingId){
        User user = entityFetcher.getCurrentUser();
        Rating rating = getRatingById(ratingId);

        if (!user.getId().equals(rating.getUser().getId())) {
            throw new RatingAccessDeniedException("You can delete only ratings posted by yourself!");
        }
        else ratingRepo.delete(rating);
    }

    @Override
    @Transactional
    public RatingDto updateRating(Long ratingId, UpdateRatingDto updateRatingDto) {
        User user = entityFetcher.getCurrentUser();
        Rating rating = getRatingById(ratingId);

        if(!user.getId().equals(rating.getUser().getId())) {
            throw new RatingAccessDeniedException("You can edit only ratings posted by yourself!");
        }
        else {
            rating.setComment(updateRatingDto.getComment());
            ratingRepo.save(rating);
        }

        return ratingMapper.mapToDto(rating);
    }

    @Override
    public RatingDto getById(Long id) {
        Rating rating = getRatingById(id);
        return ratingMapper.mapToDto(rating);
    }

    @Override
    public List<RatingDto> getRatingsByUserId(Long userId) {
        List<Rating> ratings = ratingRepo.findByUserId(userId);

        return ratings.stream().map(ratingMapper::mapToDto).toList();
    }

    @Override
    public List<RatingDto> getProductRatings(Long productId) {
        List<Rating> ratings = ratingRepo.findByProductId(productId);

        return ratings.stream().map(ratingMapper::mapToDto).collect(Collectors.toList());
    }


    private Rating getRatingById(Long ratingId){
        return ratingRepo.findById(ratingId)
                .orElseThrow(() ->  new ResourceNotFoundException("Rating not found with ID: " + ratingId));
    }
}
