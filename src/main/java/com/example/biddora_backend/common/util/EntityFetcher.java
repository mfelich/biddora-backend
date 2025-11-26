package com.example.biddora_backend.common.util;

import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.common.exception.UserNotFoundException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.product.repo.ProductRepo;
import com.example.biddora_backend.user.repo.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EntityFetcher {

    private UserRepo userRepo;
    private ProductRepo productRepo;

    public EntityFetcher(UserRepo userRepo, ProductRepo productRepo) {
        this.userRepo=userRepo;
        this.productRepo=productRepo;
    }

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
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
}
