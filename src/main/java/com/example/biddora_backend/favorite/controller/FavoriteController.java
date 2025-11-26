package com.example.biddora_backend.favorite.controller;

import com.example.biddora_backend.favorite.dto.CreateFavoriteDto;
import com.example.biddora_backend.favorite.dto.FavoriteDto;
import com.example.biddora_backend.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    ResponseEntity<List<FavoriteDto>> getAllFavorites(){
        List<FavoriteDto> favoriteDtos = favoriteService.getFavorites();

        return ResponseEntity.ok(favoriteDtos);
    }

    @GetMapping("/count/{userId}")
    ResponseEntity<Long> countUserFavorites(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(favoriteService.countUserFavorites(userId));
    }

    @PostMapping
    ResponseEntity<FavoriteDto> addToFavorite(@RequestBody CreateFavoriteDto createFavoriteDto) {
        FavoriteDto favoriteDto = favoriteService.addToFavorite(createFavoriteDto);

        return ResponseEntity.ok(favoriteDto);
    }

    @DeleteMapping("/{productId}")
    ResponseEntity<String> removeFavorite(@PathVariable("productId") Long productId) {
        favoriteService.removeFavorite(productId);

        return ResponseEntity.ok("Item removed.");
    }
}
