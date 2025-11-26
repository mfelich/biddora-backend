package com.example.biddora_backend.product.mapper;

import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto mapToDto(Product product);
}
