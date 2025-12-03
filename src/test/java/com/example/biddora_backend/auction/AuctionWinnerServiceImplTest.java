package com.example.biddora_backend.auction;

import com.example.biddora_backend.auction.entity.AuctionWinner;
import com.example.biddora_backend.auction.mapper.AuctionWinnerMapper;
import com.example.biddora_backend.auction.repo.AuctionWinnerRepo;
import com.example.biddora_backend.auction.service.impl.AuctionWinnerServiceImpl;
import com.example.biddora_backend.bid.entity.Bid;
import com.example.biddora_backend.bid.repo.BidRepo;
import com.example.biddora_backend.common.exception.BidException;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionWinnerServiceImplTest {

    @Mock
    AuctionWinnerRepo auctionWinnerRepo;

    @Mock
    BidRepo bidRepo;

    @Mock
    AuctionWinnerMapper auctionWinnerMapper;

    @Mock
    EntityFetcher entityFetcher;

    @InjectMocks
    AuctionWinnerServiceImpl auctionWinnerService;

    @Test
    void createWinner_whenWinningBidExist_returnAuctionWinner() {

        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);

        Bid winningBid = new Bid();
        winningBid.setProduct(product);
        winningBid.setUser(user);
        winningBid.setAmount(10L);

        AuctionWinner auctionWinner = new AuctionWinner();
        auctionWinner.setUser(user);
        auctionWinner.setProduct(product);
        auctionWinner.setAmount(winningBid.getAmount());

        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.of(winningBid));
        when(auctionWinnerRepo.save(any(AuctionWinner.class))).thenReturn(auctionWinner);

        AuctionWinner result = auctionWinnerService.createWinner(product);

        assertEquals(1L, result.getUser().getId());
        assertEquals(1L, result.getProduct().getId());
        assertEquals(10L, result.getAmount());
        verify(auctionWinnerRepo).save(any(AuctionWinner.class));

    }

    @Test
    void createWinner_whenWinningBidDoesNotExist_throwsBidException() {

        Product product = new Product();

        when(bidRepo.findTopByProductOrderByAmountDesc(product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auctionWinnerService.createWinner(product))
                .isInstanceOf(BidException.class);
        verify(auctionWinnerRepo, never()).save(any(AuctionWinner.class));

    }

}
