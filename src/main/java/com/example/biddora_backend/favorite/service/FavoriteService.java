package com.example.biddora_backend.favorite.service;

import com.example.biddora_backend.favorite.dto.CreateFavoriteDto;
import com.example.biddora_backend.favorite.dto.FavoriteDto;

import java.util.List;

public interface FavoriteService {
    List<FavoriteDto> getFavorites();
    FavoriteDto addToFavorite(CreateFavoriteDto createFavoriteDto);
    void removeFavorite(Long productId);
    Long countUserFavorites(Long userId);
}
