package com.example.biddora_backend.exception;

public class BidAccessDeniedException extends RuntimeException {
    public BidAccessDeniedException(String message) {
        super(message);
    }
}
