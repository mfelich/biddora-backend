package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.favoriteDtos.FavoriteDto;
import com.example.biddora_backend.entity.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    public FavoriteDto mapToDto(Favorite favorite){
        return new FavoriteDto(
                favorite.getId(),
                userMapper.mapToDto(favorite.getUser()),
                productMapper.mapToDto(favorite.getProduct()));
    }
}
