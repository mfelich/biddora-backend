package com.example.biddora_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AuctionEndedException extends RuntimeException {
    public AuctionEndedException(String message) {
        super(message);
    }
}
