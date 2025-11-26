package com.example.biddora_backend.common.exception;

public class RatingAccessDeniedException extends RuntimeException {
    public RatingAccessDeniedException(String message) {
        super(message);
    }
}
