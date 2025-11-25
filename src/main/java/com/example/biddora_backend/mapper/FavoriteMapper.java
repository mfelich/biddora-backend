package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.favoriteDtos.FavoriteDto;
import com.example.biddora_backend.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteDto mapToDto(Favorite favorite);
}
