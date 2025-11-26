package com.example.biddora_backend.common.exception;

public class ProductBadRequestException extends RuntimeException {
    public ProductBadRequestException(String message) {
        super(message);
    }
}
