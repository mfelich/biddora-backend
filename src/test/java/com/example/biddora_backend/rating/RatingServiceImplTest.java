package com.example.biddora_backend.rating;

import com.example.biddora_backend.common.exception.RatingAccessDeniedException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.common.exception.UserNotFoundException;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.rating.dto.CreateRatingDto;
import com.example.biddora_backend.rating.dto.RatingDto;
import com.example.biddora_backend.rating.dto.UpdateRatingDto;
import com.example.biddora_backend.rating.entity.Rating;
import com.example.biddora_backend.rating.mapper.RatingMapper;
import com.example.biddora_backend.rating.repo.RatingRepo;
import com.example.biddora_backend.rating.service.impl.RatingServiceImpl;
import com.example.biddora_backend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTest {

    @Mock
    RatingRepo ratingRepo;

    @Mock
    EntityFetcher entityFetcher;

    @Mock
    RatingMapper ratingMapper;

    @InjectMocks
    RatingServiceImpl ratingService;

    @Test
    void createRating_whenUserAndProductExists_returnRatingDto() {

        Long productId = 1L;

        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(productId);

        CreateRatingDto createRatingDto = new CreateRatingDto(productId, "test", 5);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setComment("test");
        rating.setRatingStars(5);
        rating.setRatingDate(LocalDateTime.now());

        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(1L);
        ratingDto.setComment("test");
        ratingDto.setRatingStars(5);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(ratingRepo.save(any(Rating.class))).thenReturn(rating);
        when(ratingMapper.mapToDto(rating)).thenReturn(ratingDto);

        RatingDto result = ratingService.createRating(createRatingDto);

        verify(ratingRepo).save(any(Rating.class));
        assertEquals(productId, product.getId());

        assertEquals("test", result.getComment());
        assertEquals(1L, result.getId());
        assertEquals(5, result.getRatingStars());

    }

    @Test
    void createRating_whenProductDoesNotExist_throwsResourceNotFoundException() {

        Long productId = 99L;

        User user = new User();
        user.setId(1L);

        CreateRatingDto createRatingDto = new CreateRatingDto(productId,"testComment",5);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(entityFetcher.getProductById(productId)).thenThrow(
                new ResourceNotFoundException("Product not found.")
        );

        assertThatThrownBy(() -> ratingService.createRating(createRatingDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found.");
        verify(ratingRepo, never()).save(any(Rating.class));
    }

    @Test
    void createRating_whenUserDoesNotExists_throwsUserNotFoundException() {

        Long productId = 1L;

        CreateRatingDto createRatingDto = new CreateRatingDto(productId, "test", 5);

        when(entityFetcher.getCurrentUser()).thenThrow(
                new UserNotFoundException("User not found.")
        );

        assertThatThrownBy(() -> ratingService.createRating(createRatingDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found.");
        verify(ratingRepo, never()).save(any(Rating.class));
    }

    @Test
    void deleteRating_whenUserIsOwnerAndRatingExists_returnVoid() {

        Long ratingId = 1L;

        User user = new User();
        user.setId(1L);

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setUser(user);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(entityFetcher.getRatingById(ratingId)).thenReturn(rating);

        ratingService.deleteRating(ratingId);

        verify(ratingRepo).delete(rating);
    }

    @Test
    void deleteRating_whenUserIsAttackerAndRatingExists_throwsRatingAccessDeniedException() {

        Long attackerId = 99L;
        Long ownerId = 1L;
        Long ratingId = 1L;

        User attacker = new User();
        attacker.setId(attackerId);

        User owner = new User();
        owner.setId(ownerId);

        Rating rating = new Rating();
        rating.setUser(owner);

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);
        when(entityFetcher.getRatingById(ratingId)).thenReturn(rating);

        assertThatThrownBy(() -> ratingService.deleteRating(ratingId))
                .isInstanceOf(RatingAccessDeniedException.class);

        verify(ratingRepo, never()).delete(any(Rating.class));
    }

    @Test
    void deleteRating_whenRatingDoesNotExist_throwsRatingNotFoundException() {

        Long ratingId = 99L;

        User user = new User();
        user.setId(1L);

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(entityFetcher.getRatingById(ratingId))
                .thenThrow(new ResourceNotFoundException("Rating not found."));

        assertThatThrownBy(() -> ratingService.deleteRating(ratingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Rating not found.");

    }

    @Test
    void updateRating_whenUserIsOwner_returnRatingDto() {

        Long ratingId = 1L;

        User owner = new User();
        owner.setId(1L);

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setUser(owner);

        UpdateRatingDto updateRatingDto = new UpdateRatingDto("test");

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(entityFetcher.getRatingById(ratingId)).thenReturn(rating);
        when(ratingRepo.save(any(Rating.class))).thenReturn(rating);
        when(ratingMapper.mapToDto(any(Rating.class))).thenReturn(new RatingDto());

        ratingService.updateRating(ratingId, updateRatingDto);

        verify(ratingRepo).save(any(Rating.class));
        assertEquals("test", rating.getComment());
    }

    @Test
    void updateRating_whenUserIsAttacker_throwsRatingAccessDeniedException() {

        Long attackerId = 99L;
        Long ownerId = 1L;
        Long ratingId = 1L;

        User attacker = new User();
        attacker.setId(attackerId);

        User owner = new User();
        owner.setId(ownerId);

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setUser(owner);

        UpdateRatingDto updateRatingDto = new UpdateRatingDto("test");

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);
        when(entityFetcher.getRatingById(ratingId)).thenReturn(rating);

        assertThatThrownBy(() -> ratingService.updateRating(ratingId, updateRatingDto))
                .isInstanceOf(RatingAccessDeniedException.class);

        verify(ratingRepo, never()).save(any(Rating.class));
    }

    @Test
    void updateRating_whenRatingDoesNotExist_throwsResourceNotFoundException() {

        Long ratingId = 99L;

        User user = new User();
        user.setId(1L);

        UpdateRatingDto updateRatingDto = new UpdateRatingDto("test");

        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(entityFetcher.getRatingById(ratingId))
                .thenThrow(new ResourceNotFoundException("Rating not found."));

        assertThatThrownBy(() -> ratingService.updateRating(ratingId, updateRatingDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Rating not found.");
        verify(ratingRepo, never()).save(any(Rating.class));
    }

}
