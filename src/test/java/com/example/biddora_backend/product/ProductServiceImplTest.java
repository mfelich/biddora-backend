package com.example.biddora_backend.product;

import com.example.biddora_backend.auction.service.AuctionWinnerService;
import com.example.biddora_backend.common.exception.ProductAccessDeniedException;
import com.example.biddora_backend.common.exception.ProductBadRequestException;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.product.dto.CreateProductDto;
import com.example.biddora_backend.product.dto.EditProductDto;
import com.example.biddora_backend.product.dto.ProductDto;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.product.mapper.ProductMapper;
import com.example.biddora_backend.product.repo.ProductRepo;
import com.example.biddora_backend.product.service.impl.ProductServiceImpl;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepo productRepo;

    @Mock
    AuctionWinnerService auctionWinnerService;

    @Mock
    ProductMapper productMapper;

    @Mock
    EntityFetcher entityFetcher;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void addProduct_whenEndTimeIsBeforeStartTime_throwsProductBadRequestException() {

        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setStartTime(startTime);
        createProductDto.setEndTime(endTime);

        verify(productRepo, never()).save(any());
        assertThatThrownBy(() -> productService.addProduct(createProductDto))
                .isInstanceOf(ProductBadRequestException.class);

    }

    @Test
    void addProduct_whenStartTimeIsBeforeEndTime_callsSaveAndReturnProductDto() {

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        User owner = new User();
        owner.setId(1L);

        CreateProductDto createProductDto = new CreateProductDto("test",10L,startTime,endTime,null);

        Product product = new Product();
        product.setUser(owner);
        product.setName(createProductDto.getName());
        product.setStartingPrice(createProductDto.getStartingPrice());
        product.setStartTime(createProductDto.getStartTime());
        product.setEndTime(createProductDto.getEndTime());
        product.setDescription(createProductDto.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setProductStatus(ProductStatus.SCHEDULED);

        Product savedProduct = new Product();
        savedProduct.setUser(owner);
        savedProduct.setName(createProductDto.getName());
        savedProduct.setStartingPrice(createProductDto.getStartingPrice());
        savedProduct.setStartTime(createProductDto.getStartTime());
        savedProduct.setEndTime(createProductDto.getEndTime());
        savedProduct.setDescription(createProductDto.getDescription());
        savedProduct.setCreatedAt(LocalDateTime.now());
        savedProduct.setProductStatus(ProductStatus.SCHEDULED);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName(createProductDto.getName());

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(productRepo.save(any())).thenReturn(savedProduct);
        when(productMapper.mapToDto(savedProduct)).thenReturn(productDto);

        ProductDto result = productService.addProduct(createProductDto);

        verify(productRepo).save(any());

        assertEquals(1L, result.getId());
        assertEquals("test", result.getName());
        assertEquals(owner.getId(), result.getUser().getId());
        assertEquals(ProductStatus.SCHEDULED, result.getProductStatus());
    }

    //get all products

    @Test
    void editProduct_whenUserIsOwner_callsSaveAndReturnProductDto() {

        Long productId = 1L;

        User owner = new User();
        owner.setId(1L);

        Product product = new Product();
        product.setId(productId);
        product.setUser(owner);
        product.setName("ProductName");
        product.setDescription("ProductDescription");

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);

        EditProductDto editProductDto = new EditProductDto("editedName","editedDescription");

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        ProductDto result = productService.editProduct(productId, editProductDto);

        verify(productRepo).save(product);
        assertEquals("editedName", result.getName());
        assertEquals("editedDescription", result.getDescription());
    }

    @Test
    void editProduct_whenUserIsAttacker_throwsProductAccessDeniedException() {

        Long attackerId = 99L;
        Long ownerId = 1L;
        Long productId = 1L;

        User attacker = new User();
        attacker.setId(attackerId);

        User owner = new User();
        owner.setId(ownerId);

        Product product = new Product();
        product.setId(productId);
        product.setUser(owner);
        product.setName("ProductName");

        EditProductDto editProductDto = new EditProductDto("editedName","editedDescription");

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);
        when(entityFetcher.getProductById(productId)).thenReturn(product);


        assertThatThrownBy(() -> productService.editProduct(productId, editProductDto))
                .isInstanceOf(ProductAccessDeniedException.class);
        verify(productRepo, never()).save(any());
        assertEquals("ProductName", product.getName());
    }

    @Test
    void deleteProduct_whenUserIsOwner_callsDeleteAndReturnString() {

        Long ownerId = 1L;
        Long productId = 1L;

        User owner = new User();
        owner.setId(ownerId);
        owner.setRole(Role.USER);

        Product product = new Product();
        product.setId(productId);
        product.setUser(owner);

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(entityFetcher.getProductById(productId)).thenReturn(product);

        String result = productService.deleteProduct(productId);

        assertEquals("Product deleted successfully.", result);
        verify(productRepo).delete(any());
    }

    @Test
    void deleteProduct_whenUserIsAdminAndDeletesAnotherUser_callsDeleteAndReturnString() {

        Long adminId = 10L;
        Long ownerId = 1L;
        Long productId = 1L;

        User admin = new User();
        admin.setId(adminId);
        admin.setRole(Role.ADMIN);

        User owner = new User();
        owner.setId(ownerId);

        Product product = new Product();
        product.setId(productId);
        product.setUser(owner);

        when(entityFetcher.getCurrentUser()).thenReturn(admin);
        when(entityFetcher.getProductById(productId)).thenReturn(product);

        String result = productService.deleteProduct(productId);

        assertEquals("Product deleted successfully.", result);
        verify(productRepo).delete(any());
    }

    @Test
    void deleteProduct_whenUserIsAttacker_throwsProductAccessDeniedException() {

        Long attackerId = 99L;
        Long ownerId = 1L;
        Long productId = 1L;

        User attacker = new User();
        attacker.setId(attackerId);
        attacker.setRole(Role.USER);

        User owner = new User();
        owner.setId(ownerId);

        Product product = new Product();
        product.setId(productId);
        product.setUser(owner);

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);
        when(entityFetcher.getProductById(productId)).thenReturn(product);

        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ProductAccessDeniedException.class);
        verify(productRepo, never()).delete(any());
    }
}
