package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.ratingDtos.CreateRatingDto;
import com.example.biddora_backend.dto.ratingDtos.RatingDto;
import com.example.biddora_backend.dto.ratingDtos.UpdateRatingDto;
import com.example.biddora_backend.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {


    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService=ratingService;
    }

    @PostMapping
    ResponseEntity<RatingDto> createRating(@RequestBody CreateRatingDto createRatingDto) {
        return ResponseEntity.ok(ratingService.createRating(createRatingDto));
    }

    @GetMapping("/{ratingId}")
    ResponseEntity<RatingDto> getById(@PathVariable Long ratingId){
        return ResponseEntity.ok(ratingService.getById(ratingId));
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<RatingDto>> getRatingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<List<RatingDto>> getProductRatings(@PathVariable("productId") Long prodcutId){
        return ResponseEntity.ok(ratingService.getProductRatings(prodcutId));
    }

    @PutMapping("/{ratingId}")
    ResponseEntity<RatingDto> updateRating(@PathVariable Long ratingId,
                                           @RequestBody UpdateRatingDto updateRatingDto) throws AccessDeniedException {
        return ResponseEntity.ok(ratingService.updateRating(ratingId,updateRatingDto));
    }

    @DeleteMapping("/{ratingId}")
    ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) throws AccessDeniedException{
        ratingService.deleteRating(ratingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
