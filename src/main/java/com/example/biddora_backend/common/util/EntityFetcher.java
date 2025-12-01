package com.example.biddora_backend.common.util;

import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.rating.entity.Rating;
import com.example.biddora_backend.rating.repo.RatingRepo;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.common.exception.UserNotFoundException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.product.repo.ProductRepo;
import com.example.biddora_backend.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFetcher {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final RatingRepo ratingRepo;

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User getCurrentUser(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return findUserByUsername(currentUsername);
    }

    public boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public Product getProductById(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id:" + productId));
        return product;
    }

    public Rating getRatingById(Long ratingId) {
        return ratingRepo.getRatingById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found."));
    }
}
