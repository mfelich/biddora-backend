package com.example.biddora_backend.exception;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime time,
                           String message,
                           String details,
                           String errorCode) {
}
