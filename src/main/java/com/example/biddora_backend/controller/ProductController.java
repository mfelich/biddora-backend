package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //Create product
    @PostMapping
    ResponseEntity<ProductDto> addProduct(@Valid @RequestBody CreateProductDto createProductDto) throws Exception{
        return ResponseEntity.ok(productService.addProduct(createProductDto));
    }

    //Get product by id
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

    //Get products by userId
    @GetMapping("/user/{userId}")
    ResponseEntity<List<ProductDto>> getProductByUser(@PathVariable Long userId){
        return ResponseEntity.ok(productService.getProductsByUser(userId));
    }

    //Edit products
    @PutMapping("/{id}")
    ResponseEntity<ProductDto> editProduct(@PathVariable("id") Long id,
                                           @RequestBody EditProductDto editProductDto) throws AccessDeniedException {
        ProductDto productDto = productService.editProduct(id,editProductDto);

        return ResponseEntity.ok(productDto);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{productId}")
    ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) throws AccessDeniedException {
        String message = productService.deleteProduct(productId);

        return ResponseEntity.ok(message);
    }
}
