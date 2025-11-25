package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingDto mapToDto(Rating rating);
}
