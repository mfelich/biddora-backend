package com.example.biddora_backend.exception;

public class RatingAccessDeniedException extends RuntimeException {
    public RatingAccessDeniedException(String message) {
        super(message);
    }
}
