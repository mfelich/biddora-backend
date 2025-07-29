package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface ProductService {
    ProductDto addProduct(CreateProductDto createProductDto) throws Exception;
    ProductDto getProductById(Long productId);
    Page<ProductDto> getALlProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name);
    ProductDto editProduct(Long productId, EditProductDto editProductDto) throws AccessDeniedException;
    String deleteProduct(Long productId) throws AccessDeniedException;
}
