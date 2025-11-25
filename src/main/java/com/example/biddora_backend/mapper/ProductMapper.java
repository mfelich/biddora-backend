package com.example.biddora_backend.mapper;

import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto mapToDto(Product product);
}
