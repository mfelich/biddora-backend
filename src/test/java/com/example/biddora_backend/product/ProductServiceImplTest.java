package com.example.biddora_backend.product;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepo productRepo;

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

        LocalDateTime startTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endTime = startTime.plusHours(1);

        CreateProductDto dto = new CreateProductDto("testName", 10L, startTime, endTime, null);

        User owner = new User();
        owner.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setUser(owner);
        product.setProductStatus(ProductStatus.SCHEDULED);
        product.setName("testName");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName(dto.getName());

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(productRepo.save(any(Product.class))).thenReturn(product);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        ProductDto result = productService.addProduct(dto);

        verify(productRepo).save(any(Product.class));

        assertEquals(1L, result.getId());
        assertEquals("testName", result.getName());
        assertEquals(ProductStatus.SCHEDULED, product.getProductStatus());
    }

    @Test
    void getAllProducts_withoutFiltersAndSort_returnsAllProductsDefaultSort() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);

        Page<Product> products = new PageImpl<>(List.of(product));

        when(productRepo.findAll(any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result =
                productService.getAllProducts(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());

        assertEquals(1, result.getTotalElements());
        verify(productRepo).findAll(pageRequestArgumentCaptor.capture());

        assertEquals("name", pageRequestArgumentCaptor.getValue().getSort().iterator().next().getProperty());
        assertEquals(Sort.Direction.ASC, pageRequestArgumentCaptor.getValue().getSort().iterator().next().getDirection());
    }

    @Test
    void getAllProducts_withSortByPriceHigh_returnsProductsSortedByPriceDesc() {

        Product product = new Product();
        product.setId(1L);
        product.setStartingPrice(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Page<Product> products = new PageImpl<>(List.of(product));

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);

        when(productRepo.findAll(any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.getAllProducts(
                Optional.empty(),
                Optional.of("price-high"),
                Optional.empty(),
                Optional.empty()
        );

        verify(productRepo).findAll(captor.capture());
        PageRequest usedPageRequest = captor.getValue();

        assertEquals(1, result.getTotalElements());
        assertEquals("startingPrice", usedPageRequest.getSort().iterator().next().getProperty());
        assertEquals(Sort.Direction.DESC, usedPageRequest.getSort().iterator().next().getDirection());

    }

    @Test
    void getAllProducts_withSortByPriceLow_returnsProductsSortedByPriceAsc() {

        Product product = new Product();
        product.setId(1L);

        ProductDto productDto = new ProductDto();
        product.setId(1L);

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);

        Page<Product> products = new PageImpl<>(List.of(product));

        when(productRepo.findAll(any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.getAllProducts(
                Optional.empty(),
                Optional.of("price-low"),
                Optional.empty(),
                Optional.empty()
        );

        verify(productRepo).findAll(captor.capture());
        PageRequest usedPageRequest = captor.getValue();

        assertEquals(1, result.getTotalElements());
        assertEquals("startingPrice", usedPageRequest.getSort().iterator().next().getProperty());
        assertEquals(Sort.Direction.ASC, usedPageRequest.getSort().iterator().next().getDirection());

    }

    @Test
    void getAllProducts_withNameFilterOnly_callsFindByNameContainingIgnoreCase() {

        String name = "testName";

        Product product = new Product();
        product.setId(1L);
        product.setName("testName");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Page<Product> products = new PageImpl<>(List.of(product));

        when(productRepo.findByNameContainingIgnoreCase(any(String.class), any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.getAllProducts(
                Optional.empty(),
                Optional.empty(),
                Optional.of(name),
                Optional.empty()
        );

        verify(productRepo).findByNameContainingIgnoreCase(any(String.class), any(PageRequest.class));
        assertEquals(1, result.getTotalElements());

    }

    @Test
    void getAllProducts_withProductTypeFilterOnly_callsFindByProductStatus() {

        Product product = new Product();
        product.setId(1L);
        product.setProductStatus(ProductStatus.OPEN);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Page<Product> products = new PageImpl<>(List.of(product));

        when(productRepo.findByProductStatus(any(ProductStatus.class), any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.getAllProducts(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(ProductStatus.OPEN)
        );

        verify(productRepo).findByProductStatus(any(ProductStatus.class), any(PageRequest.class));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllProducts_withNameAndProductTypeFilters_callsFindByNameContainingIgnoreCaseAndProductStatus() {

        Product product = new Product();
        product.setId(1L);
        product.setName("testName");
        product.setProductStatus(ProductStatus.OPEN);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        Page<Product> products = new PageImpl<>(List.of(product));

        when(productRepo.findByNameContainingIgnoreCaseAndProductStatus(any(String.class), any(ProductStatus.class), any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.getAllProducts(
                Optional.empty(),
                Optional.empty(),
                Optional.of("testName"),
                Optional.of(ProductStatus.OPEN)
        );

        verify(productRepo).findByNameContainingIgnoreCaseAndProductStatus(any(String.class),any(ProductStatus.class), any(PageRequest.class));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllProducts_withCustomPage_returnsProductsForGivenPage() {

        Product product = new Product();
        product.setId(1L);

        Page<Product> products = new PageImpl<>(List.of(product));

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);

        when(productRepo.findAll(any(PageRequest.class))).thenReturn(products);
        when(productMapper.mapToDto(product)).thenReturn(new ProductDto());

        Page<ProductDto> result = productService.getAllProducts(
                Optional.of(1),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        verify(productRepo).findAll(captor.capture());
        PageRequest usedPageRequest = captor.getValue();

        assertEquals("name", usedPageRequest.getSort().iterator().next().getProperty());
        assertEquals(Sort.Direction.ASC, usedPageRequest.getSort().iterator().next().getDirection());
        assertEquals(1, usedPageRequest.getPageNumber());
        assertEquals(1 ,result.getTotalElements());
    }

    @Test
    void getAllProducts_withSortAndFilters_combinedScenarioReturnsCorrectProducts() {

        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<ProductStatus> productStatusArgumentCaptor = ArgumentCaptor.forClass(ProductStatus.class);

        Page<Product> products = new PageImpl<>(List.of(new Product()));

        when(productRepo.findByNameContainingIgnoreCaseAndProductStatus(any(String.class), any(ProductStatus.class), any(PageRequest.class)))
                .thenReturn(products);

        when(productMapper.mapToDto(any(Product.class))).thenReturn(new ProductDto());

        productService.getAllProducts(
                Optional.of(1),
                Optional.of("price-high"),
                Optional.of("testName"),
                Optional.of(ProductStatus.OPEN)
        );

        verify(productRepo).findByNameContainingIgnoreCaseAndProductStatus(nameArgumentCaptor.capture(), productStatusArgumentCaptor.capture(), pageRequestArgumentCaptor.capture());

        assertEquals("testName", nameArgumentCaptor.getValue());
        assertEquals(ProductStatus.OPEN, productStatusArgumentCaptor.getValue());

        assertEquals(1, pageRequestArgumentCaptor.getValue().getPageNumber());
        assertEquals(Sort.Direction.DESC, pageRequestArgumentCaptor.getValue().getSort().iterator().next().getDirection());
        assertEquals("startingPrice", pageRequestArgumentCaptor.getValue().getSort().iterator().next().getProperty());

    }

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
        assertEquals("editedName", product.getName());
        assertEquals("editedDescription", product.getDescription());
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
