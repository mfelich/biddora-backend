package com.example.biddora_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AuctionNotOpenException extends RuntimeException {
    public AuctionNotOpenException(String message) {
        super(message);
    }
}
