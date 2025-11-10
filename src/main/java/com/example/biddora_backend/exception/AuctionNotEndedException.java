package com.example.biddora_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AuctionNotEndedException extends RuntimeException {
    public AuctionNotEndedException(String message) {
        super(message);
    }
}
