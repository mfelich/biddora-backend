package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.favoriteDtos.CreateFavoriteDto;
import com.example.biddora_backend.dto.favoriteDtos.FavoriteDto;

import java.util.List;

public interface FavoriteService {
    List<FavoriteDto> getFavorites();
    FavoriteDto addToFavorite(CreateFavoriteDto createFavoriteDto);
    void removeFavorite(Long productId);
    Long countUserFavorites(Long userId);
}
