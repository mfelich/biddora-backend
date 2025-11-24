package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.productDtos.CreateProductDto;
import com.example.biddora_backend.dto.productDtos.EditProductDto;
import com.example.biddora_backend.dto.productDtos.ProductDto;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.entity.Role;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.ProductBadRequestException;
import com.example.biddora_backend.exception.ProductAccessDeniedException;
import com.example.biddora_backend.exception.ResourceNotFoundException;
import com.example.biddora_backend.mapper.ProductMapper;
import com.example.biddora_backend.repo.ProductRepo;
import com.example.biddora_backend.service.AuctionWinnerService;
import com.example.biddora_backend.service.ProductService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final AuctionWinnerService auctionWinnerService;
    private final ProductMapper productMapper;
    private final EntityFetcher entityFetcher;

    public ProductServiceImpl(ProductRepo productRepo,AuctionWinnerService auctionWinnerService, EntityFetcher entityFetcher, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.auctionWinnerService=auctionWinnerService;
        this.entityFetcher = entityFetcher;
        this.productMapper = productMapper;
    }

    //Create product
    @Override
    @Transactional
    public ProductDto addProduct(CreateProductDto createProductDto) throws Exception {

        if (!createProductDto.getEndTime().isAfter(createProductDto.getStartTime())) {
            throw new ProductBadRequestException("End time must be after start time!");
        }

        User user = entityFetcher.getCurrentUser();
        Product product = new Product();

        product.setUser(user);
        product.setName(createProductDto.getName());
        product.setStartingPrice(createProductDto.getStartingPrice());
        product.setStartTime(createProductDto.getStartTime());
        product.setEndTime(createProductDto.getEndTime());
        product.setDescription(createProductDto.getDescription());
        product.setCreatedAt(LocalDateTime.now());
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
    public List<ProductDto> getProductsByUser(Long userId) {
        User user = entityFetcher.getUserById(userId);
        List<Product> products = productRepo.findAllByUserId(user.getId());

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("This user has no products!");
        }

        return products.stream().map(productMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> getAllProducts(Optional<Integer> page, Optional<String> sortBy, Optional<String> name, Optional<ProductStatus> productType) {
        String sortField = sortBy.orElse("name");
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortBy.isPresent()) {
            if (sortBy.get().equals("price-high")) {
                sortField = "startingPrice";
                direction = Sort.Direction.DESC;
            } else if (sortBy.get().equals("price-low")) {
                sortField = "startingPrice";
                direction = Sort.Direction.ASC;
            }
        }

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                12,
                direction,
                sortField
        );

        Page<Product> products;

        if (name.isPresent() && productType.isPresent()) {
            products = productRepo.findByNameContainingIgnoreCaseAndProductStatus(
                    name.get(), productType.get(), pageRequest
            );
        } else if (name.isPresent()) {
            products = productRepo.findByNameContainingIgnoreCase(name.get(), pageRequest);
        } else if (productType.isPresent()) {
            products = productRepo.findByProductStatus(productType.get(), pageRequest);
        } else {
            products = productRepo.findAll(pageRequest);
        }

        return products.map(productMapper::mapToDto);
    }


    //Edit product by id (ADMIN cannot edit product)
    @Override
    @Transactional
    public ProductDto editProduct(Long productId, EditProductDto editProductDto) {
        User user = entityFetcher.getCurrentUser();
        Product product = entityFetcher.getProductById(productId);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new ProductAccessDeniedException("You can not edit this product!");
        }

        product.setName(editProductDto.getName());
        product.setDescription(editProductDto.getDescription());
        productRepo.save(product);

        return productMapper.mapToDto(product);
    }

    //Delete product by id (ADMIN can also delete everyones product)
    @Override
    @Transactional
    public String deleteProduct(Long productId) {
        User user = entityFetcher.getCurrentUser();
        Product product = entityFetcher.getProductById(productId);

        if (!user.getRole().equals(Role.ADMIN) &&
                !user.getId().equals(product.getUser().getId())) {
            throw new ProductAccessDeniedException("You can not delete this product!");
        }

        productRepo.delete(product);
        return "Product deleted successfully!";
    }

    @Override
    @Transactional
    public void openScheduledAuctions() {
        List<Product> toOpen = productRepo.findByProductStatusAndStartTimeLessThanEqual(ProductStatus.SCHEDULED, LocalDateTime.now());
        toOpen.forEach(p -> p.setProductStatus(ProductStatus.OPEN));
        productRepo.saveAll(toOpen);
    }

    @Override
    @Transactional
    public void closeExpiredAuctions() {
        List<Product> toClose = productRepo.findByProductStatusAndEndTimeLessThanEqual(ProductStatus.OPEN, LocalDateTime.now());
        List<Product> allToSave = new ArrayList<>();

        toClose.forEach(p -> {
            try {
                p.setAuctionWinner(auctionWinnerService.createWinner(p));
                p.setProductStatus(ProductStatus.CLOSED);
                allToSave.add(p);
            } catch (Exception e) {
                System.err.println("Greška kod kreiranja pobjednika za proizvod id=" + p.getId() + ": " + e.getMessage());
            }
        });

        productRepo.saveAll(allToSave);
    }
}
