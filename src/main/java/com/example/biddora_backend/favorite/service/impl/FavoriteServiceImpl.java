package com.example.biddora_backend.favorite.service.impl;

import com.example.biddora_backend.favorite.dto.CreateFavoriteDto;
import com.example.biddora_backend.favorite.dto.FavoriteDto;
import com.example.biddora_backend.favorite.entity.Favorite;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.common.exception.ResourceAlreadyExistsException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.favorite.mapper.FavoriteMapper;
import com.example.biddora_backend.favorite.repo.FavoriteRepo;
import com.example.biddora_backend.favorite.service.FavoriteService;
import com.example.biddora_backend.common.util.EntityFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepo favoriteRepo;
    private final FavoriteMapper favoriteMapper;
    private final EntityFetcher entityFetcher;

    @Override
    public List<FavoriteDto> getFavorites() {
        User user = entityFetcher.getCurrentUser();

        List<Favorite> favorites = favoriteRepo.findByUserId(user.getId());

        return favorites
                .stream()
                .map(favoriteMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteDto addToFavorite(CreateFavoriteDto createFavoriteDto) {

        User user = entityFetcher.getCurrentUser();

        Optional<Favorite> favorite = favoriteRepo.findByUserIdAndProductId(user.getId(), createFavoriteDto.getProductId());

        if (!favorite.isEmpty()) {
            throw new ResourceAlreadyExistsException("This product is already in your favorites.");
        }

        Favorite newFavorite = new Favorite();
        newFavorite.setUser(user);
        newFavorite.setProduct(entityFetcher.getProductById(createFavoriteDto.getProductId()));

        FavoriteDto savedFavorite = favoriteMapper.mapToDto(favoriteRepo.save(newFavorite));

        return savedFavorite;
    }

    @Override
    public void removeFavorite(Long productId) {
        User user = entityFetcher.getCurrentUser();

        Favorite favorite = favoriteRepo.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite for product with ID:" + productId + " not found!"));

        favoriteRepo.delete(favorite);
    }

    @Override
    public Long countUserFavorites(Long userId) {
        return favoriteRepo.countByUserId(userId);
    }
}
