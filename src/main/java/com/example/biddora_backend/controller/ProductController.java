package com.example.biddora_backend.controller;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService=productService;
    }

    //Create product
    @PostMapping
    ResponseEntity<ProductDto> addProduct(@Valid @RequestBody CreateProductDto createProductDto) throws Exception{
        ProductDto productDto = productService.addProduct(createProductDto);

        return ResponseEntity.ok(productDto);
    }

    //Get product by id
    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        ProductDto productDto = productService.getProductById(productId);

        return ResponseEntity.ok(productDto);
    }

    //Get all product in pages, also possible to get product by their name
    @GetMapping("/all")
    ResponseEntity<Page<ProductDto>> getALlProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name){
        Page<ProductDto> productDtoPage = productService.getALlProducts(page, sortBy, name);

        return ResponseEntity.ok(productDtoPage);
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
