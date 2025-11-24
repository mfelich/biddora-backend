package com.example.biddora_backend.exception;

public class ProductBadRequestException extends RuntimeException {
    public ProductBadRequestException(String message) {
        super(message);
    }
}
