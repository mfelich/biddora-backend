package com.example.biddora_backend.auction.service.impl;

import com.example.biddora_backend.auction.dto.AuctionWinnerDto;
import com.example.biddora_backend.auction.entity.AuctionWinner;
import com.example.biddora_backend.bid.entity.Bid;
import com.example.biddora_backend.common.exception.ResourceNotFoundException;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.common.exception.BidException;
import com.example.biddora_backend.auction.mapper.AuctionWinnerMapper;
import com.example.biddora_backend.auction.repo.AuctionWinnerRepo;
import com.example.biddora_backend.bid.repo.BidRepo;
import com.example.biddora_backend.auction.service.AuctionWinnerService;
import com.example.biddora_backend.common.util.EntityFetcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionWinnerServiceImpl implements AuctionWinnerService {

    private final AuctionWinnerRepo auctionWinnerRepo;
    private final BidRepo bidRepo;
    private final AuctionWinnerMapper auctionWinnerMapper;
    private final EntityFetcher entityFetcher;

        @Override
        @Transactional
        public AuctionWinner createWinner(Product product) {

            Optional<Bid> winningBidOpt = bidRepo.findTopByProductOrderByAmountDesc(product);

            if (winningBidOpt.isEmpty()) {
                throw new BidException("There are no bids for the product with ID: " + product.getId());
            }

            Bid winningBid = winningBidOpt.get();

            AuctionWinner auctionWinner = new AuctionWinner();
            auctionWinner.setUser(winningBid.getUser());
            auctionWinner.setProduct(product);
            auctionWinner.setAmount(winningBid.getAmount());

            return auctionWinnerRepo.save(auctionWinner);

        }

        @Override
        public AuctionWinnerDto getAuctionWinner(Long productId) {

            Product product = entityFetcher.getProductById(productId);

            if (!product.getProductStatus().equals(ProductStatus.CLOSED)) {
                throw new BidException("The auction for this product has not ended yet.");
            }

            Optional<AuctionWinner> auctionWinner = auctionWinnerRepo.findByProductId(productId);

            if (auctionWinner.isEmpty()) {
                throw new ResourceNotFoundException("Winner not found.");
            }

            return auctionWinnerMapper.mapToDto(auctionWinner.get());
        }
}
