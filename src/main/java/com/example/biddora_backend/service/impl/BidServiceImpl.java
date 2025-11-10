package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.dto.bidDtos.BidDto;
import com.example.biddora_backend.dto.bidDtos.CreateBidDto;
import com.example.biddora_backend.entity.Bid;
import com.example.biddora_backend.entity.Product;
import com.example.biddora_backend.entity.ProductStatus;
import com.example.biddora_backend.entity.User;
import com.example.biddora_backend.exception.ResourceNotFoundException;
import com.example.biddora_backend.handlers.SocketConnectionHandler;
import com.example.biddora_backend.mapper.BidMapper;
import com.example.biddora_backend.repo.BidRepo;
import com.example.biddora_backend.service.BidService;
import com.example.biddora_backend.service.util.EntityFetcher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepo bidRepo;
    private final BidMapper bidMapper;
    private final EntityFetcher entityFetcher;
    private final SocketConnectionHandler socketConnectionHandler;

    public BidServiceImpl(BidRepo bidRepo, BidMapper bidMapper, EntityFetcher entityFetcher, SocketConnectionHandler socketConnectionHandler) {
        this.bidRepo = bidRepo;
        this.bidMapper = bidMapper;
        this.entityFetcher = entityFetcher;
        this.socketConnectionHandler=socketConnectionHandler;
    }
    
    @Override
    @Transactional
    public BidDto placeBid(CreateBidDto createBidDto) throws AccessDeniedException {

        Product product = entityFetcher.getProductById(createBidDto.getProductId());
        User user = entityFetcher.getCurrentUser();

        Long highest = bidRepo.findTopByProductOrderByAmountDesc(product).map(Bid::getAmount).orElse(product.getStartingPrice());

        if (product.getProductStatus() != ProductStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bidding is not allowed. Auction is not active.");
        }

        if (product.getUser().equals(user)) {
            throw new AccessDeniedException("You cannot bid on your own product.");
        }

        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Auction has ended.");
        }

        if (createBidDto.getAmount() <= highest) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bid must be higher than current highest bid.");
        }

        //Creating bid
        Bid bid = new Bid();
        bid.setAmount(Long.valueOf(createBidDto.getAmount()));
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

        if (bids.isEmpty()){
            throw new ResourceNotFoundException("This product has no bids!");
        }

        return bids.map(bidMapper::mapToDto);
    }
}
