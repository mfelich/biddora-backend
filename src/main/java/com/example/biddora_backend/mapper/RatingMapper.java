package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.entity.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public RatingMapper(UserMapper userMapper,ProductMapper productMapper) {
        this.userMapper=userMapper;
        this.productMapper=productMapper;
    }

    public RatingDto mapToDto(Rating rating) {
        return new RatingDto(
                rating.getId(),
                userMapper.mapToDto(rating.getUser()),
                productMapper.mapToDto(rating.getProduct()),
                rating.getComment(),
                rating.getRatingStars(),
                rating.getRatingDate());
    }


}
