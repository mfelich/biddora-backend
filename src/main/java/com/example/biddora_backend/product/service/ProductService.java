package com.example.biddora_backend.product.service;

import com.example.biddora_backend.product.dto.CreateProductDto;
import com.example.biddora_backend.product.dto.EditProductDto;
import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.product.enums.ProductStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface    ProductService {
    ProductDto addProduct(CreateProductDto createProductDto);
    ProductDto getProductById(Long productId);
    List<ProductDto> getProductsByUser(Long userId);
    Page<ProductDto> getAllProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name, Optional<ProductStatus> productType);
    ProductDto editProduct(Long productId, EditProductDto editProductDto);
    String deleteProduct(Long productId);
}
