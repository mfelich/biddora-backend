package com.example.biddora_backend.service;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.ProductStatus;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface    ProductService {
    ProductDto addProduct(CreateProductDto createProductDto) throws Exception;
    ProductDto getProductById(Long productId);
    List<ProductDto> getProductsByUser(Long userId);
    Page<ProductDto> getAllProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name, Optional<ProductStatus> productType);
    ProductDto editProduct(Long productId, EditProductDto editProductDto) throws AccessDeniedException;
    String deleteProduct(Long productId) throws AccessDeniedException;
    void openScheduledAuctions();
    void closeExpiredAuctions();
}
