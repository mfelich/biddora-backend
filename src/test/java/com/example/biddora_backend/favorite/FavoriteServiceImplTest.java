package com.example.biddora_backend.favorite;

import com.example.biddora_backend.common.exception.ResourceAlreadyExistsException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.favorite.dto.CreateFavoriteDto;
import com.example.biddora_backend.favorite.dto.FavoriteDto;
import com.example.biddora_backend.favorite.entity.Favorite;
import com.example.biddora_backend.favorite.mapper.FavoriteMapper;
import com.example.biddora_backend.favorite.repo.FavoriteRepo;
import com.example.biddora_backend.favorite.service.impl.FavoriteServiceImpl;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @Mock
    FavoriteRepo favoriteRepo;

    @Mock
    FavoriteMapper favoriteMapper;

    @Mock
    EntityFetcher entityFetcher;

    @InjectMocks
    FavoriteServiceImpl favoriteService;

    @Test
    void addToFavorite_whenProductIsNotInFavorites_returnFavoriteDto() {

        Long userId = 1L;
        Long productId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);

        Favorite favorite = new Favorite();
        favorite.setProduct(product);
        favorite.setUser(user);

        CreateFavoriteDto createFavoriteDto = new CreateFavoriteDto(productId);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(favoriteRepo.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());
        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(favoriteRepo.save(any(Favorite.class))).thenReturn(favorite);
        when(favoriteMapper.mapToDto(favorite)).thenReturn(new FavoriteDto());

        FavoriteDto result = favoriteService.addToFavorite(createFavoriteDto);

        verify(favoriteRepo).save(any(Favorite.class));
        assertNotNull(result);

    }

    @Test
    void addToFavorite_whenProductIsInFavorites_throwsResourceAlreadyExistsException() {

        Long userId = 1L;
        Long productId = 99L;

        User user = new User();
        user.setId(userId);

        Favorite favorite = new Favorite();
        favorite.setUser(user);

        CreateFavoriteDto createFavoriteDto = new CreateFavoriteDto(productId);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(favoriteRepo.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(favorite));

        assertThatThrownBy(() -> favoriteService.addToFavorite(createFavoriteDto))
                .isInstanceOf(ResourceAlreadyExistsException.class);
        verify(favoriteRepo, never()).save(any(Favorite.class));

    }

    @Test
    void removeFavorite_whenProductIsInFavorites_returnVoid() {

        Long userId = 1L;
        Long productId = 1L;

        User user = new User();
        user.setId(1L);

        Favorite favorite = new Favorite();
        favorite.setUser(user);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(favoriteRepo.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(favorite));

        favoriteService.removeFavorite(productId);

        verify(favoriteRepo).delete(any(Favorite.class));

    }

    @Test
    void removeFavorite_whenProductIsNotInFavorites_throwsResourceNotFoundException() {

        Long userId = 1L;
        Long productId = 99L;

        User user = new User();
        user.setId(1L);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(favoriteRepo.findByUserIdAndProductId(userId, productId))
                .thenThrow(new ResourceNotFoundException("Favorite not found."));

        assertThatThrownBy(() -> favoriteService.removeFavorite(productId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Favorite not found.");
        verify(favoriteRepo, never()).delete(any(Favorite.class));

    }
}
