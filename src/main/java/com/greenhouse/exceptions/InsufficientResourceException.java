package com.greenhouse.exceptions;

public class InsufficientResourceException extends RuntimeException {
    public InsufficientResourceException(String message) {
        super(message);
    }
}
