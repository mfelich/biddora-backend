package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.favoriteDtos.CreateFavoriteDto;
import com.example.biddora_backend.dto.favoriteDtos.FavoriteDto;
import com.example.biddora_backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

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
