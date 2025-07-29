package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.mapper.ProductMapper;
import com.example.biddora_backend.repo.ProductRepo;
import com.example.biddora_backend.service.ProductService;
import com.example.biddora_backend.service.util.EntityFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final EntityFetcher entityFetcher;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo,EntityFetcher entityFetcher,ProductMapper productMapper) {
        this.productRepo=productRepo;
        this.entityFetcher=entityFetcher;
        this.productMapper=productMapper;
    }

    //Create product
    @Override
    public ProductDto addProduct(CreateProductDto createProductDto) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        
        if (!createProductDto.getEndTime().isAfter(createProductDto.getStartTime())){
            throw new Exception("End time must be after start time!");
        }

        User user = entityFetcher.getCurrentUser();
        Product product = new Product();

        product.setUser(user);
        product.setName(createProductDto.getName());
        product.setStartingPrice(createProductDto.getStartingPrice());
        product.setStartTime(createProductDto.getStartTime());
        product.setEndTime(createProductDto.getEndTime());
        product.setProductStatus(ProductStatus.SCHEDULED);

        Product savedProduct = productRepo.save(product);

        return productMapper.mapToDto(savedProduct);
    }

    //Get product by id
    @Override
    public ProductDto getProductById(Long productId) {
        Product product = entityFetcher.getProductById(productId);

        return productMapper.mapToDto(product);
    }

    @Override
    public Page<ProductDto> getALlProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name) {
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                12,
                Sort.Direction.ASC,
                sortBy.orElse("name")
        );
        Page<Product> products;

        if (name.isPresent()){
            products = productRepo.findByNameContainingIgnoreCase(name.get(),pageRequest);
        }
        else {
            products = productRepo.findAll(pageRequest);
        }

        return products.map(productMapper::mapToDto);
    }

    //Edit product by id (ADMIN cannot edit product)
    @Override
    public ProductDto editProduct(Long productId, EditProductDto editProductDto) throws AccessDeniedException {
        User user = entityFetcher.getCurrentUser();
        Product product = entityFetcher.getProductById(productId);

        if (!product.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You can not edit this product!");
        }

        product.setName(editProductDto.getName());
        productRepo.save(product);

        return productMapper.mapToDto(product);
    }

    //Delete product by id (ADMIN can also delete everyones product)
    @Override
    public String deleteProduct(Long productId) throws AccessDeniedException {
        User user = entityFetcher.getCurrentUser();
        Product product = entityFetcher.getProductById(productId);

        if (!user.getRole().equals(Role.ADMIN) &&
        !user.getId().equals(product.getUser().getId())){
            throw new AccessDeniedException("You can not delete this product!");
        }

        productRepo.delete(product);
        return "Product deleted successfully!";
    }
}
