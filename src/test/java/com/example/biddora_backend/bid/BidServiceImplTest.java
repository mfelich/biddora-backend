package com.example.biddora_backend.bid;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.dto.CreateBidDto;
import com.example.biddora_backend.bid.entity.Bid;
import com.example.biddora_backend.bid.mapper.BidMapper;
import com.example.biddora_backend.bid.repo.BidRepo;
import com.example.biddora_backend.bid.service.impl.BidServiceImpl;
import com.example.biddora_backend.common.exception.BidAccessDeniedException;
import com.example.biddora_backend.common.exception.BidException;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.common.handlers.SocketConnectionHandler;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceImplTest {

    @Mock
    BidRepo bidRepo;

    @Mock
    BidMapper bidMapper;

    @Mock
    EntityFetcher entityFetcher;

    @Mock
    SocketConnectionHandler socketConnectionHandler;

    @InjectMocks
    BidServiceImpl bidService;

    @Test
    void placeBid_whenValidRequest_savesBidAndReturnsBidDto() {

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Long productId = 1L;
        Long userId = 1L;
        Long productUserId = 99L;

        User user = new User();
        user.setId(userId);

        User productUser = new User();
        productUser.setId(productUserId);

        Bid bid = new Bid();
        bid.setAmount(10L);

        BidDto bidDto = new BidDto();
        bidDto.setId(1L);

        CreateBidDto createBidDto = new CreateBidDto(productId, 11L);

        Product product = new Product();
        product.setId(1L);
        product.setProductStatus(ProductStatus.OPEN);
        product.setUser(productUser);
        product.setStartTime(startTime);
        product.setEndTime(endTime);
        product.setStartingPrice(10L);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.of(bid));
        when(bidRepo.save(any(Bid.class))).thenReturn(bid);
        when(bidMapper.mapToDto(any(Bid.class))).thenReturn(bidDto);

        BidDto result = bidService.placeBid(createBidDto);

        verify(bidRepo).save(any(Bid.class));
        assertEquals(1L, result.getId());

    }

    @Test
    void placeBid_whenProductIsNotOpen_throwsBidException() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setProductStatus(ProductStatus.SCHEDULED);
        product.setStartingPrice(10L);

        User user = new User();
        user.setId(1L);

        CreateBidDto createBidDto = new CreateBidDto(productId, 11L);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidService.placeBid(createBidDto))
                .isInstanceOf(BidException.class);
        verify(bidRepo, never()).save(any(Bid.class));

    }

    @Test
    void placeBid_whenUserIsProductOwner_throwsBidAccessDeniedException() {

        Long productId = 1L;

        User owner = new User();

        Product product = new Product();
        product.setProductStatus(ProductStatus.OPEN);
        product.setUser(owner);

        CreateBidDto createBidDto = new CreateBidDto(productId, 11L);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidService.placeBid(createBidDto))
                .isInstanceOf(BidAccessDeniedException.class);
        verify(bidRepo, never()).save(any(Bid.class));

    }

    @Test
    void placeBid_whenAuctionHasEnded_throwsBidException() {

        Long productId = 1L;
        LocalDateTime startTime = LocalDateTime.now().minusDays(2);
        LocalDateTime endTime = LocalDateTime.now().minusDays(1);

        User owner = new User();
        owner.setId(1L);

        User user = new User();
        user.setId(10L);

        Product product = new Product();
        product.setId(productId);
        product.setProductStatus(ProductStatus.OPEN);
        product.setUser(owner);
        product.setStartTime(startTime);
        product.setEndTime(endTime);

        CreateBidDto createBidDto = new CreateBidDto(productId, 11L);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidService.placeBid(createBidDto))
                .isInstanceOf(BidException.class);
        verify(bidRepo, never()).save(any(Bid.class));

    }

    @Test
    void placeBid_whenBidAmountIsLowerOrEqualToHighest_throwsBidException() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setStartTime(LocalDateTime.now().minusDays(1));
        product.setEndTime(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setId(1L);

        Bid bid = new Bid();
        bid.setProduct(product);
        bid.setAmount(10L);

        CreateBidDto createBidDto = new CreateBidDto(productId, 10L);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(entityFetcher.getCurrentUser()).thenReturn(user);
        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.of(bid));

        assertThatThrownBy(() -> bidService.placeBid(createBidDto))
                .isInstanceOf(BidException.class);
        verify(bidRepo, never()).save(any(Bid.class));

    }

    @Test
    void getBidsByProductId_whenValidRequest_returnsPagedBidDtos() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        Bid bid = new Bid();
        bid.setId(1L);

        BidDto bidDto = new BidDto();
        bidDto.setId(1L);

        Page<Bid> bids = new PageImpl<>(List.of(bid));

        PageRequest pageRequest = PageRequest.of(
                0,
                12
        );

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(bidRepo.findByProductIdOrderByAmountDesc(productId, pageRequest)).thenReturn(bids);
        when(bidMapper.mapToDto(bid)).thenReturn(bidDto);

        Page<BidDto> result = bidService.getBidsByProductId(productId, Optional.empty());

        assertEquals(1, result.getTotalElements());

    }

    @Test
    void getBidsByProductId_whenProductNotFound_throwsResourceNotFoundException() {

        Long productId = 99L;

        when(entityFetcher.getProductById(productId))
                .thenThrow(new ResourceNotFoundException("Product not found."));

        assertThatThrownBy(() -> bidService.getBidsByProductId(productId, Optional.empty()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found.");
        verify(bidRepo, never()).findByProductIdOrderByAmountDesc(any(Long.class), any(PageRequest.class));
    }

    @Test
    void getBidsByProductId_whenPageNotProvided_usesDefaultPageZero() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        Bid bid = new Bid();

        Page<Bid> bids = new PageImpl<>(List.of(bid));

        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);

        when(entityFetcher.getProductById(productId)).thenReturn(product);
        when(bidRepo.findByProductIdOrderByAmountDesc(eq(productId), pageRequestArgumentCaptor.capture())).thenReturn(bids);
        when(bidMapper.mapToDto(bid)).thenReturn(any(BidDto.class));

        bidService.getBidsByProductId(productId, Optional.empty());

        PageRequest usedPageRequest = pageRequestArgumentCaptor.getValue();

        assertEquals(0, usedPageRequest.getPageNumber());
        assertEquals(12, usedPageRequest.getPageSize());

    }

}
