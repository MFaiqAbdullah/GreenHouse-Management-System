package com.greenhouse.exceptions;

public class InvalidPlantDataException extends RuntimeException {
    public InvalidPlantDataException(String message) {
        super(message);
    }
}
