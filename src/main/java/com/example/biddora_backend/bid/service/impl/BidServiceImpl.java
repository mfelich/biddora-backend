package com.example.biddora_backend.bid.service.impl;

import com.example.biddora_backend.bid.dto.BidDto;
import com.example.biddora_backend.bid.dto.CreateBidDto;
import com.example.biddora_backend.bid.entity.Bid;
import com.example.biddora_backend.common.exception.BidAccessDeniedException;
import com.example.biddora_backend.common.exception.BidException;
import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.product.enums.ProductStatus;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.common.handlers.SocketConnectionHandler;
import com.example.biddora_backend.bid.mapper.BidMapper;
import com.example.biddora_backend.bid.repo.BidRepo;
import com.example.biddora_backend.bid.service.BidService;
import com.example.biddora_backend.common.util.EntityFetcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepo bidRepo;
    private final BidMapper bidMapper;
    private final EntityFetcher entityFetcher;
    private final SocketConnectionHandler socketConnectionHandler;

    @Override
    @Transactional
    public BidDto placeBid(CreateBidDto createBidDto) {

        Product product = entityFetcher.getProductById(createBidDto.getProductId());
        User user = entityFetcher.getCurrentUser();

        Long highest = bidRepo.findTopByProductOrderByAmountDesc(product).map(Bid::getAmount).orElse(product.getStartingPrice());

        if (product.getProductStatus() != ProductStatus.OPEN) {
            throw new BidException("Bidding is not allowed. Auction is not open.");
        }

        if (product.getUser().equals(user)) {
            throw new BidAccessDeniedException("You cannot bid on your own product.");
        }

        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            throw new BidException("Auction ended at:" + product.getEndTime());
        }

        if (createBidDto.getAmount() <= highest) {
            throw new BidException("Bid must be higher than current highest bid: " + highest);
        }

        Bid bid = new Bid();
        bid.setAmount(createBidDto.getAmount());
        bid.setUser(user);
        bid.setProduct(product);
        bid.setTimestamp(LocalDateTime.now());

        Bid savedBid = bidRepo.save(bid);
        BidDto mappedBid = bidMapper.mapToDto(savedBid);

        try {
            socketConnectionHandler.sendBidToProduct(mappedBid);
            System.out.println("Sending bid via WS: " + mappedBid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappedBid;
    }

    @Override
    public Page<BidDto> getBidsByProductId(Long productId, Optional<Integer> page){
        Product product = entityFetcher.getProductById(productId);

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                12
        );

        Page<Bid> bids = bidRepo.findByProductIdOrderByAmountDesc(product.getId(), pageRequest);

        return bids.map(bidMapper::mapToDto);
    }
}
