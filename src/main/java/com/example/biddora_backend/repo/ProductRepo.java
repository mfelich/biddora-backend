package com.example.biddora_backend.repo;

import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findByProductStatusAndStartTimeLessThanEqual(ProductStatus productStatus, LocalDateTime localDateTime);
    List<Product> findByProductStatusAndEndTimeLessThanEqual(ProductStatus productStatus, LocalDateTime localDateTime);
}
