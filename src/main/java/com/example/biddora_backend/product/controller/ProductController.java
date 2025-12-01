package com.example.biddora_backend.product.controller;

import com.example.biddora_backend.product.dto.CreateProductDto;
import com.example.biddora_backend.product.dto.EditProductDto;
import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    ResponseEntity<ProductDto> addProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.addProduct(createProductDto));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> name,
            @RequestParam Optional<ProductStatus> productType) {

        Page<ProductDto> products = productService.getAllProducts(page, sortBy, name, productType);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<ProductDto>> getProductByUser(@PathVariable Long userId){
        return ResponseEntity.ok(productService.getProductsByUser(userId));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> editProduct(@PathVariable("id") Long id,
                                           @RequestBody EditProductDto editProductDto) {
        ProductDto productDto = productService.editProduct(id,editProductDto);

        return ResponseEntity.ok(productDto);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{productId}")
    ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        String message = productService.deleteProduct(productId);

        return ResponseEntity.ok(message);
    }
}
