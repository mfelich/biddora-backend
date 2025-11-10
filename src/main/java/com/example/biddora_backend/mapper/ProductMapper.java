package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final UserMapper userMapper;

    @Autowired
    public ProductMapper(UserMapper userMapper) {
        this.userMapper=userMapper;
    }

    public ProductDto mapToDto(Product product){
        return new ProductDto(product.getId(),
                product.getName(),
                product.getStartingPrice(),
                product.getStartTime(),
                product.getEndTime(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getProductStatus(),userMapper.mapToDto(product.getUser()));
    }
}
