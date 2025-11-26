package com.example.biddora_backend.common.exception;

public class BidAccessDeniedException extends RuntimeException {
    public BidAccessDeniedException(String message) {
        super(message);
    }
}
