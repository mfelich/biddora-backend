package com.example.biddora_backend.rating.mapper;

import com.example.biddora_backend.rating.dto.RatingDto;
import com.example.biddora_backend.rating.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingDto mapToDto(Rating rating);
}
