package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.ratingDtos.CreateRatingDto;
import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.dto.ratingDtos.UpdateRatingDto;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.Rating;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.AccountException;
import com.example.biddora_backend.exception.ResourceNotFoundException;
import com.example.biddora_backend.mapper.RatingMapper;
import com.example.biddora_backend.repo.RatingRepo;
import com.example.biddora_backend.service.RatingService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {


    private RatingRepo ratingRepo;
    private EntityFetcher entityFetcher;
    private RatingMapper ratingMapper;

    public RatingServiceImpl(RatingRepo ratingRepo, EntityFetcher entityFetcher, RatingMapper ratingMapper) {
        this.ratingRepo=ratingRepo;
        this.entityFetcher=entityFetcher;
        this.ratingMapper=ratingMapper;
    }


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
    public void deleteRating(Long ratingId) throws AccessDeniedException{
        User user = entityFetcher.getCurrentUser();
        Rating rating = getRatingById(ratingId);

        if (!user.getId().equals(rating.getUser().getId())) {
            throw new AccessDeniedException("You can delete only ratings posted by yourself!");
        }
        else ratingRepo.delete(rating);
    }

    @Override
    @Transactional
    public RatingDto updateRating(Long ratingId, UpdateRatingDto updateRatingDto) throws AccessDeniedException{
        User user = entityFetcher.getCurrentUser();
        Rating rating = getRatingById(ratingId);

        if(!user.getId().equals(rating.getUser().getId())) {
            throw new AccessDeniedException("You can edit only ratings posted by yourself!");
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

        if (ratings.isEmpty()) {
            throw new ResourceNotFoundException("This user has no ratings!");
        }
        return ratings.stream().map(ratingMapper::mapToDto).toList();
    }

    @Override
    public List<RatingDto> getProductRatings(Long productId) {
        List<Rating> ratings = ratingRepo.findByProductId(productId);

        if (ratings.isEmpty()){
            throw new ResourceNotFoundException("This product has no ratings!");
        }
        return ratings.stream().map(ratingMapper::mapToDto).collect(Collectors.toList());
    }


    private Rating getRatingById(Long ratingId){
        return ratingRepo.findById(ratingId)
                .orElseThrow(() ->  new ResourceNotFoundException("Rating does not exist with id:" + ratingId));
    }
}
