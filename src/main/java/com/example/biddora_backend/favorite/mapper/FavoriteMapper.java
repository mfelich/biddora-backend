package com.example.biddora_backend.favorite.mapper;

import com.example.biddora_backend.favorite.dto.FavoriteDto;
import com.example.biddora_backend.favorite.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteDto mapToDto(Favorite favorite);
}
